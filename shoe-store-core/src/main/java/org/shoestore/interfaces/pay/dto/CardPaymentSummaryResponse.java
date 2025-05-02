package org.shoestore.interfaces.pay.dto;


import org.shoestore.domain.model.pay.IssuingBank;

public class CardPaymentSummaryResponse {

    private IssuingBank issuingBank;

    private Long totalAmount;

    public IssuingBank getIssuingBank() {

        return issuingBank;
    }

    public Long getTotalAmount() {

        return totalAmount;
    }

    public CardPaymentSummaryResponse(IssuingBank issuingBank, Long totalAmount) {

        this.issuingBank = issuingBank;
        this.totalAmount = totalAmount;
    }
}
