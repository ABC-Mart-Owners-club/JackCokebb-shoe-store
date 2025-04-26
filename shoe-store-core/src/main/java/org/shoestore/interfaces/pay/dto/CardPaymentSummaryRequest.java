package org.shoestore.interfaces.pay.dto;

import java.time.LocalDateTime;
import org.shoestore.domain.model.pay.Payment.IssuingBank;

public class CardPaymentSummaryRequest {

    private IssuingBank issuingBank;

    private LocalDateTime from;

    private LocalDateTime to;

    public CardPaymentSummaryRequest(IssuingBank issuingBank, LocalDateTime from,
        LocalDateTime to) {
        this.issuingBank = issuingBank;
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {

        return from;
    }

    public LocalDateTime getTo() {

        return to;
    }

    public IssuingBank getIssuingBank() {

        return issuingBank;
    }
}
