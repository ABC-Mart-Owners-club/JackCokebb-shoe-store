package org.shoestore.interfaces.pay.dto;


import org.shoestore.domain.model.pay.PayMethod;

public class CardPaymentSummaryResponse {

    private PayMethod payMethod;

    private Long totalAmount;

    public PayMethod getPayMethod() {

        return payMethod;
    }

    public Long getTotalAmount() {

        return totalAmount;
    }

    public CardPaymentSummaryResponse(PayMethod payMethod, Long totalAmount) {

        this.payMethod = payMethod;
        this.totalAmount = totalAmount;
    }
}
