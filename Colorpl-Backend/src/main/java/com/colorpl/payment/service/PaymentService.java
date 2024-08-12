package com.colorpl.payment.service;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.payment.controller.PaymentController;
import com.colorpl.payment.dto.ReceiptDTO;
import com.colorpl.payment.dto.SeatInfoDTO;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.reservation.dto.ReservationDTO;
import com.colorpl.reservation.dto.ReservationDetailDTO;
import com.colorpl.reservation.repository.ReservationDetailRepository;
import com.colorpl.reservation.service.ReservationService;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.domain.ShowSchedule;
import com.colorpl.show.repository.ShowDetailRepository;
import com.colorpl.show.repository.ShowScheduleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.request.UserToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final MemberRepository memberRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final ShowScheduleRepository showScheduleRepository;
    private final ShowDetailRepository showDetailRepository;

    @Value("${bootpay.api.application-id}")
    private String applicationId;

    @Value("${bootpay.api.private-key}")
    private String privateKey;

    @Value("${bootpay.api.url}")
    private String apiUrl;

    private Bootpay bootpay;

    @PostConstruct
    public void init() {
        this.bootpay = new Bootpay(applicationId, privateKey);
    }

    public String getAccessToken() throws Exception {
        HashMap<String, Object> res = bootpay.getAccessToken();
        if (res.get("error_code") == null) {
            return (String) res.get("access_token");
        } else {
            throw new Exception("Error getting access token: " + res.get("message"));
        }
    }

    public HashMap<String, Object> getReceipt(String receiptId, String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");
        return bootpay.getReceipt(receiptId);
    }

    public HashMap<String, Object> confirmPayment(String receiptId, String authorizationHeader) throws Exception {
        String paymentToken = authorizationHeader.replace("Bearer ", "");

        HashMap<String, Object> response = bootpay.confirm(receiptId);

        Integer memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        member.addReceiptId(receiptId);
        memberRepository.save(member);

        return response;
    }

    @Transactional
    public HashMap<String, Object> confirmPaymentFromReceipt(String receiptId, String authorizationHeader) {
        try {
            // 영수증 조회
            HashMap<String, Object> receiptDetails = bootpay.getReceipt(receiptId);

            // metadata 추출
            Map<String, Object> metadata = (Map<String, Object>) receiptDetails.get("metadata");
            if (metadata == null) {
                throw new RuntimeException("Metadata is null in payment data.");
            }

            // selectedSeatList 타입 확인 및 변환
            Object selectedSeatListObject = metadata.get("selectedSeatList");
            if (!(selectedSeatListObject instanceof List)) {
                throw new RuntimeException("Selected seat list is not of type List.");
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> selectedSeatList = (List<Map<String, Object>>) selectedSeatListObject;

            Long showScheduleId = ((Number) metadata.get("showScheduleId")).longValue();
            if (showScheduleId == null) {
                throw new RuntimeException("Show schedule ID is null in metadata.");
            }

            // ReservationDTO 생성
            List<ReservationDetailDTO> reservationDetails = selectedSeatList.stream()
                    .map(seat -> {
                        Byte row = (byte) ((Number) seat.get("row")).intValue();
                        Byte col = (byte) ((Number) seat.get("col")).intValue();

                        return ReservationDetailDTO.builder()
                                .row(row)
                                .col(col)
                                .showScheduleId(showScheduleId)
                                .build();
                    })
                    .collect(Collectors.toList());

            // Convert price to string and handle potential null value
            String amount = String.valueOf(receiptDetails.get("price"));
            String comment = (String) receiptDetails.get("order_name");

            ReservationDTO reservationDTO = ReservationDTO.builder()
                    .date(LocalDateTime.now()) // 예제일 경우 현재 시간으로 설정
                    .amount(amount) // 가격 설정
                    .comment(comment) // 주석 설정
                    .isRefunded(false) // 기본값 설정
                    .reservationDetails(reservationDetails)
                    .build();

            Integer memberId = memberService.getCurrentMemberId();

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));
            member.addReceiptId(receiptId);
            memberRepository.save(member);

            reservationService.createReservation(memberId, reservationDTO);

            // 결제 승인
            HashMap<String, Object> confirmResponse = bootpay.confirm(receiptId);
            return confirmResponse;
        } catch (RuntimeException e) {
            // 예외 발생 시 롤백
            log.error("RuntimeException occurred: ", e);
            throw new RuntimeException("Failed to confirm payment: " + e.getMessage(), e);
        } catch (Exception e) {
            // 일반 예외 발생 시 롤백
            log.error("Exception occurred: ", e);
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }


    //    public HashMap<String, Object> confirmPayment(String receiptId, String authorizationHeader) throws Exception {
//        HashMap<String, Object> response = new HashMap<>();
//
//        try {
//            // 결제 승인
//            response = bootpay.confirm(receiptId);
//
//            Integer memberId = memberService.getCurrentMemberId();
//            Member member = memberRepository.findById(memberId)
//                    .orElseThrow(MemberNotFoundException::new);
//
//            // 영수증 ID 추가
//            member.addReceiptId(receiptId);
//            memberRepository.save(member);
//
//
//            // 결제 데이터에서 예매 정보 추출
//            Map<String, Object> metadata = (Map<String, Object>) response.get("metadata");
//            List<String> selectedSeatList = (List<String>) metadata.get("selectedSeatList");
//            Long showScheduleId = ((Number) metadata.get("showScheduleId")).longValue();
//
//            // ReservationDTO 생성
//            ReservationDTO reservationDTO = createReservationDTO(selectedSeatList, showScheduleId, response);
//            reservationService.createReservation(memberId, reservationDTO);
//
//        } catch (Exception e) {
//            // 예외 발생 시 트랜잭션 롤백을 명시적으로 처리할 수 있음
//            throw new RuntimeException("Failed to confirm payment: " + e.getMessage(), e);
//        }
//
//        return response;
//    }
    public HashMap<String, Object> cancelPayment(Cancel cancel, String authorizationHeader) throws Exception {
        String paymentToken = authorizationHeader.replace("Bearer ", "");
        Integer memberId = memberService.getCurrentMemberId();

        HashMap<String, Object> response = bootpay.receiptCancel(cancel);

        return response;
    }



    @Transactional
    public ResponseEntity<String> removeReceiptIdFromMember(String receiptId) throws MemberNotFoundException {
        Integer memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        member.removeReceiptId(receiptId);
        memberRepository.save(member);

        return ResponseEntity.ok("Receipt ID removed successfully.");
    }


    public HashMap<String, Object> getUserToken(UserToken userToken, String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");
        return bootpay.getUserToken(userToken);
    }

    public List<ReceiptDTO> getAllReceiptsForMember(String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");

        // 현재 로그인된 회원의 ID를 가져옵니다.
        Integer memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 회원의 영수증 ID 목록을 가져옵니다.
        List<String> receiptIds = member.getReceiptIds();

        System.out.println("Receipt IDs: " + receiptIds);

        return receiptIds.stream()
                .map(receiptId -> {
                    try {
                        return getReceiptDetail(receiptId);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to retrieve receipt details for ID: " + receiptId, e);
                    }
                })
                .collect(Collectors.toList());
    }
public ReceiptDTO getReceiptDetail(String receiptId) throws Exception {

    HashMap<String, Object> receiptDetails = bootpay.getReceipt(receiptId);
    String orderName = (String) receiptDetails.get("order_name");

    ShowDetail showDetail = findShowDetailByOrderName(orderName).orElse(null);

    if (showDetail == null) {
        return ReceiptDTO.builder()
                .receiptId(receiptId)
                .orderName(orderName)
                .purchasedAt((String) receiptDetails.get("purchased_at"))
                .price((int) receiptDetails.get("price"))
                .statusLocale((String) receiptDetails.get("status_locale"))
                .seats(null)
                .showDateTime(null)
                .theaterName(null)
                .showDetailPosterImagePath(null)
                .build();
    }


    ShowSchedule showSchedule = showDetail.getShowSchedules().stream()
            .findFirst()
            .orElse(null);


    List<ReservationDetail> reservationDetails = reservationDetailRepository.findByShowScheduleId(showSchedule != null ? showSchedule.getId() : null);


    List<SeatInfoDTO> seatInfos = reservationDetails.stream()
            .map(reservationDetail -> SeatInfoDTO.builder()
                    .row(reservationDetail.getRow())
                    .col(reservationDetail.getCol())
                    .build())
            .collect(Collectors.toList());

    return ReceiptDTO.builder()
            .receiptId(receiptId)
            .orderName(orderName)
            .purchasedAt((String) receiptDetails.get("purchased_at"))
            .price((int) receiptDetails.get("price"))
            .statusLocale((String) receiptDetails.get("status_locale"))
            .seats(seatInfos)  // 좌석 정보 리스트 추가
            .showDateTime(showSchedule != null ? showSchedule.getDateTime() : null)
            .theaterName(showDetail.getHall() != null ? showDetail.getHall().getTheater().getName() : null)
            .showDetailPosterImagePath(showDetail.getPosterImagePath())
            .build();
}
    private Optional<ShowDetail> findShowDetailByOrderName(String orderName) {

        return showDetailRepository.findAll().stream()
                .filter(showDetail -> showDetail.getName().equals(orderName))
                .findFirst();
    }

}
