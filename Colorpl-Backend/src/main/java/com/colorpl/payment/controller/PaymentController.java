package com.colorpl.payment.controller;

import com.colorpl.payment.dto.CancelRequest;
import com.colorpl.payment.dto.PaymentRequest;
import com.colorpl.payment.dto.UserTokenRequest;
import com.colorpl.payment.service.BootpayService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private BootpayService bootpayService;

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

    @PostMapping("/request-payment")
    @Operation(summary = " 결제 요청", description = "client-id 이슈로 사용하지 않음")
    public ResponseEntity<HashMap<String, Object>> requestPayment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PaymentRequest paymentRequest) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            HashMap<String, Object> response = bootpayService.requestPayment(paymentRequest, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            HashMap<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while processing the payment request.");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/receipt/{id}")
    @Operation(summary = " 영수증 조회", description = "영수증 id로 영수증 정보를 받아오는 api")
    public ResponseEntity<?> getReceipt(
            @RequestHeader("Authorization") String authorizationHeader,
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
    @Operation(summary = "결제 승인", description = "영수증 id를 param으로 받아서 결제를 승인하는 api")
    public ResponseEntity<?> confirmPayment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String receiptId) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            HashMap<String, Object> response = bootpayService.confirmPayment(receiptId, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new HashMap<String, String>() {{
                        put("error", "Failed to confirm payment: " + e.getMessage());
                    }}
            );
        }
    }

    @PostMapping("/cancel")
    @Operation(summary = " 결제 취소", description = "취소할 영수증 정보를 json으로 받아서 결제 취소하는 api")
    public ResponseEntity<?> cancelPayment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CancelRequest cancelRequest) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            HashMap<String, Object> response = bootpayService.cancelPayment(cancelRequest, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new HashMap<String, String>() {{
                        put("error", "Failed to cancel payment: " + e.getMessage());
                    }}
            );
        }
    }

//    @PostMapping("/request/user/token")
//    @Operation(summary = " 구매자 토큰 발급받기", description = "구매자 토큰을 발급받는 api")
//    public ResponseEntity<?> getUserToken(
//            @RequestHeader("Authorization") String authorizationHeader,
//            @RequestBody UserTokenRequest userTokenRequest) {
//        try {
//            String token = authorizationHeader.replace("Bearer ", "");
//            HashMap<String, Object> response = bootpayService.getUserToken(userTokenRequest, token);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    new HashMap<String, String>() {{
//                        put("error", "Failed to get user token: " + e.getMessage());
//                    }}
//            );
//        }
//    }

    @PostMapping("/request/user/token")
    @Operation(summary = "구매자 토큰 발급받기", description = "구매자 토큰을 발급받는 api")
    public ResponseEntity<?> getUserToken(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestBody UserTokenRequest userTokenRequest) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            HashMap<String, Object> response = bootpayService.getUserToken(userTokenRequest, token);
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

}
