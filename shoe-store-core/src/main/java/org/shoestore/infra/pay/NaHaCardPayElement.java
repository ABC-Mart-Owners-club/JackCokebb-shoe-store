package org.shoestore.infra.pay;

import org.shoestore.domain.model.pay.PayElement;
import org.shoestore.domain.model.pay.PayMethod;

public class NaHaCardPayElement extends PayElement {

    public NaHaCardPayElement(Long payAmount) {

        super(PayMethod.NAHA_CARD, payAmount);
    }

    public NaHaCardPayElement(Long id, Long payAmount) {

        super(id, PayMethod.NAHA_CARD, payAmount);
    }

    public static NaHaCardPayElement init(Long payAmount) {

        return new NaHaCardPayElement(payAmount);
    }

    @Override
    public void processPayment() {

    }

    @Override
    public void cancelPayment() {

    }
}
