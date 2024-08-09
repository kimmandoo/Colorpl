package com.colorpl.payment.dto;

import kr.co.bootpay.model.request.Cancel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CancelRequest extends Cancel {
    public String receiptId;
    public Double cancelPrice;
    public Double cancelTaxFree;
    public String cancelId;
    public String cancelUsername;
    public String cancelMessage;
    public RefundData refund;


}