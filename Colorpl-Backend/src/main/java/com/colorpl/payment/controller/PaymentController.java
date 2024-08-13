package com.colorpl.payment.controller;

import com.colorpl.payment.dto.ReceiptDTO;
import com.colorpl.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.request.UserToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    private static final Logger log = LoggerFactory.getLogger(kr.co.bootpay.service.PaymentService.class);
    @GetMapping("/token")
    @Operation(summary = "사용자 토큰 발급", description = "Back-end 결제 api 사용을 위한 Authorization Token을 발급, 발급받은 토큰은 별도의 Bearer을 안붙이고 사용 가능")
    public ResponseEntity<?> getAccessToken() {
        try {
            String token = paymentService.getAccessToken();
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
            HashMap<String, Object> response = paymentService.getReceipt(id, authorizationHeader);
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
    @Transactional
    @Operation(summary = "결제 승인", description = "영수증 ID를 param으로 받아서 결제를 승인하는 API")
    public ResponseEntity<?> confirmPayment(
            @RequestHeader("Pay-Authorization") String authorizationHeader,
            @RequestParam String receiptId) {
        try {
            HashMap<String, Object> response = paymentService.confirmPaymentFromReceipt(receiptId, authorizationHeader);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // 롤백된 경우 오류 처리
            log.error("RuntimeException occurred during payment confirmation: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new HashMap<String, String>() {{
                        put("error", "Failed to confirm payment: " + e.getMessage());
                    }}
            );
        } catch (Exception e) {
            // 일반 예외 발생 시 오류 처리
            log.error("Exception occurred during payment confirmation: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new HashMap<String, String>() {{
                        put("error", "Unexpected error: " + e.getMessage());
                    }}
            );
        }
    }

    @PostMapping("/cancel")
    @Operation(summary = "결제 취소", description = "취소할 영수증 정보를 json으로 받아서 결제 취소하는 API")
    public ResponseEntity<?> cancelPayment(
            @RequestHeader("Pay-Authorization") String authorizationHeader,
            @RequestBody Cancel cancel) {
        try {
            HashMap<String, Object> response = paymentService.cancelPayment(cancel, authorizationHeader);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new HashMap<String, String>() {{
                        put("error", "Failed to cancel payment: " + e.getMessage());
                    }}
            );
        }
    }
    @DeleteMapping("/delete/receipt/{receiptId}")
    @Operation(summary = "결제 내역 삭제", description = "특정 결제 내역을 삭제하는 API")
    public ResponseEntity<?> deleteReceipt(@PathVariable String receiptId,
                                           @RequestHeader("Pay-Authorization") String authorizationHeader) {
        try {
            // 결제 내역 삭제를 시도
            paymentService.removeReceiptIdFromMember(receiptId);

            // 성공 메시지 반환
            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("message", "Receipt deleted successfully.");
            }});
        } catch (IllegalArgumentException e) {
            // 영수증 ID가 존재하지 않는 경우의 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new HashMap<String, String>() {{
                        put("error", "Failed to delete receipt: " + e.getMessage());
                    }}
            );
        } catch (Exception e) {
            // 다른 예외 발생 시의 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new HashMap<String, String>() {{
                        put("error", "Failed to delete receipt: " + e.getMessage());
                    }}
            );
        }
    }


    @PostMapping("/request/user/token")
    @Operation(summary = "구매자 토큰 발급받기", description = "구매자 토큰을 발급받는 api")
    public ResponseEntity<?> getUserToken(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserToken userToken) {
        try {
            HashMap<String, Object> response = paymentService.getUserToken(userToken, authorizationHeader);
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
            List<ReceiptDTO> receipts = paymentService.getAllReceiptsForMember(authorizationHeader);
            return ResponseEntity.ok(receipts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

}
