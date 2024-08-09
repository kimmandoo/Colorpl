package com.colorpl.payment.dto;

import kr.co.bootpay.model.response.data.PaymentData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class RefundData extends PaymentData {
    public String bankAccount;
    public String bankUsername;
    public String bankcode;

    public RefundData() {
    }
}
