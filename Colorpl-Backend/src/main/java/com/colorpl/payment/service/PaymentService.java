package com.colorpl.payment.service;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.payment.dto.ReceiptDTO;
import com.colorpl.payment.dto.SeatInfoDTO;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.reservation.repository.ReservationDetailRepository;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.domain.ShowSchedule;
import com.colorpl.show.repository.ShowDetailRepository;
import com.colorpl.show.repository.ShowScheduleRepositoryImpl;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.request.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final ShowScheduleRepositoryImpl showScheduleRepository;
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

//    public HashMap<String, Object> cancelPayment(Cancel cancel, String authorizationHeader) throws Exception {
//        String paymentToken = authorizationHeader.replace("Bearer ", "");
//
//        HashMap<String, Object> response = bootpay.receiptCancel(cancel);
//
//        Integer memberId = memberService.getCurrentMemberId();
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(MemberNotFoundException::new);
//
//        member.removeReceiptId(cancel.receiptId);
//        memberRepository.save(member);
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

//    public List<ReceiptDTO> getAllReceiptsForMember(String authorizationHeader) throws Exception {
//        String token = authorizationHeader.replace("Bearer ", "");
//
//        Integer memberId = memberService.getCurrentMemberId();
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(MemberNotFoundException::new);
//
//        List<String> receiptIds = member.getReceiptIds();
//
//        return receiptIds.stream()
//                .map(receiptId -> {
//                    try {
//                        HashMap<String, Object> receiptDetails = bootpay.getReceipt(receiptId);
//
//                        return ReceiptDTO.builder()
//                                .receiptId(receiptId)
//                                .orderName((String) receiptDetails.get("order_name"))
//                                .purchasedAt((String) receiptDetails.get("purchased_at"))
//                                .price((int) receiptDetails.get("price"))
//                                .statusLocale((String) receiptDetails.get("status_locale"))
//                                .build();
//                    } catch (Exception e) {
//                        throw new RuntimeException("Failed to retrieve receipt details for ID: " + receiptId);
//                    }
//                })
//                .collect(Collectors.toList());
//    }
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
