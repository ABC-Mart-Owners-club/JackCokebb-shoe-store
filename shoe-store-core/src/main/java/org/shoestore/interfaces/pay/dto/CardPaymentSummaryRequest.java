package org.shoestore.interfaces.pay.dto;

import java.time.LocalDateTime;
import org.shoestore.domain.model.pay.PayMethod;

public class CardPaymentSummaryRequest {

    private PayMethod payMethod;

    private LocalDateTime from;

    private LocalDateTime to;

    public CardPaymentSummaryRequest(PayMethod payMethod, LocalDateTime from,
        LocalDateTime to) {
        this.payMethod = payMethod;
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {

        return from;
    }

    public LocalDateTime getTo() {

        return to;
    }

    public PayMethod getPayMethod() {

        return payMethod;
    }
}
