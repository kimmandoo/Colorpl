package com.colorpl.payment.service;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.payment.dto.ReceiptDTO;
import jakarta.annotation.PostConstruct;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.request.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

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

    public HashMap<String, Object> cancelPayment(Cancel cancel, String authorizationHeader) throws Exception {
        String paymentToken = authorizationHeader.replace("Bearer ", "");

        HashMap<String, Object> response = bootpay.receiptCancel(cancel);

        Integer memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        member.removeReceiptId(cancel.receiptId);
        memberRepository.save(member);

        return response;
    }

    public HashMap<String, Object> getUserToken(UserToken userToken, String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");
        return bootpay.getUserToken(userToken);
    }

    public List<ReceiptDTO> getAllReceiptsForMember(String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");

        Integer memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        List<String> receiptIds = member.getReceiptIds();

        return receiptIds.stream()
                .map(receiptId -> {
                    try {
                        HashMap<String, Object> receiptDetails = bootpay.getReceipt(receiptId);

                        return ReceiptDTO.builder()
                                .receiptId(receiptId)
                                .orderName((String) receiptDetails.get("order_name"))
                                .purchasedAt((String) receiptDetails.get("purchased_at"))
                                .price((int) receiptDetails.get("price"))
                                .statusLocale((String) receiptDetails.get("status_locale"))
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to retrieve receipt details for ID: " + receiptId);
                    }
                })
                .collect(Collectors.toList());
    }
}
