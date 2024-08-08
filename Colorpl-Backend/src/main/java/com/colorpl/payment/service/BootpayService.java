package com.colorpl.payment.service;

import com.colorpl.payment.dto.CancelRequest;
import com.colorpl.payment.dto.PaymentRequest;
import com.colorpl.payment.dto.UserTokenRequest;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import kr.co.bootpay.Bootpay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BootpayService {

    @Value("${bootpay.api.application-id}")
    private String applicationId;

    @Value("${bootpay.api.private-key}")
    private String privateKey;

    @Value("${bootpay.api.url}")
    private String apiUrl;

    private Bootpay bootpay;

    private RestTemplate restTemplate = new RestTemplate();

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
    public HashMap<String, Object> requestPayment(PaymentRequest request, String token) throws Exception {
        // Build the payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("application_id", applicationId);
        payload.put("order_name", request.getOrderName());
        payload.put("order_id", request.getOrderId());
        payload.put("price", request.getTotalPrice());
        payload.put("user", request.getUser());
        payload.put("items", request.getItems());

        // Add metadata if needed
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("1", "abcdef");
        metadata.put("2", "abcdef55");
        metadata.put("3", 1234);
        payload.put("metadata", metadata);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<HashMap> response = restTemplate.exchange(
                apiUrl + "/request/payment",
                HttpMethod.POST,
                entity,
                HashMap.class
        );

        return response.getBody();
    }


    public HashMap<String, Object> getReceipt(String receiptId, String token) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<HashMap> response = restTemplate.exchange(
                apiUrl + "/receipt/" + receiptId,
                HttpMethod.GET,
                entity,
                HashMap.class
        );
        return response.getBody();
    }

    public HashMap<String, Object> confirmPayment(String receiptId, String token) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<HashMap> response = restTemplate.exchange(
                apiUrl + "/confirm",
                HttpMethod.POST,
                entity,
                HashMap.class
        );
        return response.getBody();
    }
//
//    public HashMap<String, Object> requestPayment(PaymentRequest paymentRequest, String token) throws Exception {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<PaymentRequest> entity = new HttpEntity<>(paymentRequest, headers);
//        ResponseEntity<HashMap> response = restTemplate.exchange(
//                apiUrl + "/request/payment",
//                HttpMethod.POST,
//                entity,
//                HashMap.class
//        );
//        return response.getBody();
//    }

    public HashMap<String, Object> cancelPayment(CancelRequest cancelRequest, String token) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CancelRequest> entity = new HttpEntity<>(cancelRequest, headers);
        ResponseEntity<HashMap> response = restTemplate.exchange(
                apiUrl + "/cancel",
                HttpMethod.POST,
                entity,
                HashMap.class
        );
        return response.getBody();
    }

    public HashMap<String, Object> getUserToken(UserTokenRequest userTokenRequest, String token) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (userTokenRequest.getUserId() == null || userTokenRequest.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be blank");
        }

        HttpEntity<UserTokenRequest> entity = new HttpEntity<>(userTokenRequest, headers);
        ResponseEntity<HashMap> response = restTemplate.exchange(
            apiUrl + "/request/user/token",
            HttpMethod.POST,
            entity,
            HashMap.class
        );
        return response.getBody();
    }
}
