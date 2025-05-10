package org.shoestore.domain.model.pay;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.shoestore.infra.pay.DaiHyunCardPayElement;

public abstract class PayElement {

    private Long id;

    private PayMethod payMethod;

    private Long payAmount;


    public abstract void processPayment();

    public abstract void cancelPayment();


    public PayMethod getPayMethod() {

        return payMethod;
    }

    public Long getId() {
        return id;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public PayElement(Long id, PayMethod payMethod, Long payAmount) {
        this.id = id;
        this.payMethod = payMethod;
        this.payAmount = payAmount;
    }

    public PayElement(PayMethod payMethod, Long payAmount) {
        this.id = getNewId();
        this.payMethod = payMethod;
        this.payAmount = payAmount;
    }

    private Long getNewId() {

        return UUID.randomUUID().getMostSignificantBits() - LocalDateTime.now().toInstant(
            ZoneOffset.UTC).toEpochMilli();
    }
}
