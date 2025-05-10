package org.shoestore.interfaces.pay.dto;

import org.shoestore.domain.model.pay.PayMethod;

public class PayElementDto {

    private PayMethod payMethod;

    private Long payAmount;

    public PayElementDto(PayMethod payMethod, Long payAmount) {
        this.payMethod = payMethod;
        this.payAmount = payAmount;
    }

    public PayMethod getPayMethod() {

        return payMethod;
    }

    public Long getPayAmount() {

        return payAmount;
    }
}
