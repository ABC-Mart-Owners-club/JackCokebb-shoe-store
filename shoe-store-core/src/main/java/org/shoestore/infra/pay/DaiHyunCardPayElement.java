package org.shoestore.infra.pay;

import org.shoestore.domain.model.pay.PayElement;
import org.shoestore.domain.model.pay.PayMethod;

public class DaiHyunCardPayElement extends PayElement {

    public DaiHyunCardPayElement(Long id, Long payAmount) {

        super(id, PayMethod.DAIHYUN_CARD, payAmount);
    }

    public DaiHyunCardPayElement(Long payAmount) {

        super(PayMethod.DAIHYUN_CARD, payAmount);
    }

    public static DaiHyunCardPayElement init(Long payAmount) {

        return new DaiHyunCardPayElement(payAmount);
    }

    @Override
    public void processPayment() {

    }

    @Override
    public void cancelPayment() {

    }
}
