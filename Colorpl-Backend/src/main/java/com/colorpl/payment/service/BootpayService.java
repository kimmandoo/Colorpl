package com.colorpl.payment.service;

import com.colorpl.payment.dto.CancelRequest;
import com.colorpl.payment.dto.UserTokenRequest;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.request.UserToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BootpayService {

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

    public HashMap<String, Object> getReceipt(String receiptId, String token) throws Exception {
        return bootpay.getReceipt(receiptId);
    }

    public HashMap<String, Object> confirmPayment(String receiptId, String token) throws Exception {
        return bootpay.confirm(receiptId);
    }

    public HashMap<String, Object> cancelPayment(Cancel cancel, String token) throws Exception {
        return bootpay.receiptCancel(cancel);
    }

    public HashMap<String, Object> getUserToken(UserToken userToken, String token) throws Exception {
        return bootpay.getUserToken(userToken);
    }

}
