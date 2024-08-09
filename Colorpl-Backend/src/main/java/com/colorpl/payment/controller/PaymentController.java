package com.colorpl.payment.controller;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.payment.dto.ReceiptDTO;
import jakarta.transaction.Transactional;
import java.util.List;
import kr.co.bootpay.model.request.Cancel;
import com.colorpl.payment.dto.CancelRequest;
import com.colorpl.payment.service.BootpayService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.HashMap;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.request.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {


    private final BootpayService bootpayService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    @GetMapping("/token")
    @Operation(summary = "사용자 토큰 발급", description = "Back-end 결제 api 사용을 위한 Authorization Token을 발급, 발급받은 토큰은 별도의 Bearer을 안붙이고 사용 가능")
    public ResponseEntity<?> getAccessToken() {
        try {
            String token = bootpayService.getAccessToken();
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new HashMap<String, String>() {{
                    put("error", "Failed to get access token: " + e.getMessage());
                }}
            );
        }
    }

    @GetMapping("/receipt/{id}")
    @Operation(summary = "영수증 조회", description = "영수증 id로 영수증 정보를 받아오는 api")
    public ResponseEntity<?> getReceipt(
        @RequestHeader("Pay-Authorization") String authorizationHeader,
        @PathVariable String id) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            HashMap<String, Object> response = bootpayService.getReceipt(id, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new HashMap<String, String>() {{
                    put("error", "Failed to get receipt: " + e.getMessage());
                }}
            );
        }
    }


    @PostMapping("/confirm")
    @Operation(summary = "결제 승인", description = "영수증 ID를 param으로 받아서 결제를 승인하는 API")
    @Transactional
    public ResponseEntity<?> confirmPayment(
        @RequestHeader("Pay-Authorization") String authorizationHeader,
        @RequestParam String receiptId) {
        try {
            // Authorization 헤더에서 결제 토큰 추출
            String paymentToken = authorizationHeader.replace("Bearer ", "");

            // 결제 승인 처리
            HashMap<String, Object> response = bootpayService.confirmPayment(receiptId, paymentToken);

            // 결제가 성공적으로 이루어졌다면
                // 현재 로그인한 사용자의 ID 가져오기
                Integer memberId = memberService.getCurrentMemberId();
                // memberId를 사용하여 Member 객체 찾기
                Member member = memberRepository.findById(memberId)
                    .orElseThrow(MemberNotFoundException::new);
                // 사용자 정보에 영수증 ID 추가
                member.addReceiptId(receiptId);
                // 변경사항 저장
                memberRepository.save(member);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new HashMap<String, String>() {{
                    put("error", "Failed to confirm payment: " + e.getMessage());
                }}
            );
        }
    }




//    @PostMapping("/confirm")
//    @Operation(summary = "결제 승인", description = "영수증 ID를 param으로 받아서 결제를 승인하는 api")
//    public ResponseEntity<?> confirmPayment(
//        @RequestHeader("Authorization") String authorizationHeader,
//        @RequestParam String receiptId) {
//        try {
//            String token = authorizationHeader.replace("Bearer ", "");
//            HashMap<String, Object> response = bootpayService.confirmPayment(receiptId, token);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                new HashMap<String, String>() {{
//                    put("error", "Failed to confirm payment: " + e.getMessage());
//                }}
//            );
//        }
//    }

//    @PostMapping("/cancel")
//    @Operation(summary = "결제 취소", description = "취소할 영수증 정보를 json으로 받아서 결제 취소하는 api")
//    public ResponseEntity<?> cancelPayment(
//        @RequestHeader("Authorization") String authorizationHeader,
//        @RequestBody Cancel cancel) {
//        try {
//            String token = authorizationHeader.replace("Bearer ", "");
//
//            HashMap<String, Object> response = bootpayService.cancelPayment(cancel, token);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                new HashMap<String, String>() {{
//                    put("error", "Failed to cancel payment: " + e.getMessage());
//                }}
//            );
//        }
//    }
    @PostMapping("/cancel")
    @Operation(summary = "결제 취소", description = "취소할 영수증 정보를 json으로 받아서 결제 취소하는 API")
    public ResponseEntity<?> cancelPayment(
        @RequestHeader("Pay-Authorization") String authorizationHeader,
        @RequestBody Cancel cancel) {
        try {
            // Authorization 헤더에서 결제 토큰 추출
            String paymentToken = authorizationHeader.replace("Bearer ", "");

            // 현재 로그인한 사용자의 ID 가져오기
            Integer memberId = memberService.getCurrentMemberId();

            // 결제 취소 처리
            HashMap<String, Object> response = bootpayService.cancelPayment(cancel, paymentToken);

            // 결제가 성공적으로 취소되었으면

                // memberId를 사용하여 Member 객체 찾기
                Member member = memberRepository.findById(memberId)
                    .orElseThrow(MemberNotFoundException::new);

                // 사용자 정보에서 취소된 영수증 ID 제거
                member.removeReceiptId(cancel.receiptId);

                // 변경사항 저장
                memberRepository.save(member);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new HashMap<String, String>() {{
                    put("error", "Failed to cancel payment: " + e.getMessage());
                }}
            );
        }
    }
//
    @PostMapping("/request/user/token")
    @Operation(summary = "구매자 토큰 발급받기", description = "구매자 토큰을 발급받는 api")
    public ResponseEntity<?> getUserToken(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestBody UserToken userToken) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            HashMap<String, Object> response = bootpayService.getUserToken(userToken, token);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new HashMap<String, String>() {{
                    put("error", e.getMessage());
                }}
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new HashMap<String, String>() {{
                    put("error", "Failed to get user token: " + e.getMessage());
                }}
            );
        }
    }
    @GetMapping("/receipts")
    @Transactional
    @Operation(summary = "구매자의 모든 결제내역 조회", description = "구매자의 모든 영수증 내역을 받아오는 api")
    public ResponseEntity<?> getAllReceiptsForMember(
        @RequestHeader("Pay-Authorization") String authorizationHeader
        ) {
        try {

            String token = authorizationHeader.replace("Bearer ", "");
            // 특정 멤버를 조회

            Integer memberId = memberService.getCurrentMemberId();
            Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

            // 멤버의 모든 영수증 ID 목록을 가져옴
            List<String> receiptIds = member.getReceiptIds();

            // 각 영수증을 조회하여 필요한 필드만 추출
            List<ReceiptDTO> receipts = receiptIds.stream()
                .map(receiptId -> {
                    try {
                        // 영수증 상세 정보 조회
                        HashMap<String, Object> receiptDetails = bootpayService.getReceipt(receiptId,token);

                        // 필요한 필드만 추출하여 ReceiptDTO로 변환
                        return ReceiptDTO.builder()
                            .orderName((String) receiptDetails.get("order_name"))
                            .purchasedAt((String) receiptDetails.get("purchased_at"))
                            .price((int) receiptDetails.get("price"))
                            .statusLocale((String) receiptDetails.get("status_locale"))
                            .build();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to retrieve receipt details for ID: " + receiptId);
                    }
                })
                .toList();

            // 결과 반환
            return ResponseEntity.ok(receipts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }


//    @GetMapping("/{memberId}/receipts")
//    @Transactional
//    public ResponseEntity<?> getAllReceiptsForMember(@PathVariable Integer memberId) {
//        try {
//            // 특정 멤버를 조회
//            Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + memberId));
//
//            // 멤버의 모든 영수증 ID 목록을 가져옴
//            List<String> receiptIds = member.getReceiptIds();
//
//            // 결과 반환
//            return ResponseEntity.ok(receiptIds);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Error: " + e.getMessage());
//        }
//    }


}
