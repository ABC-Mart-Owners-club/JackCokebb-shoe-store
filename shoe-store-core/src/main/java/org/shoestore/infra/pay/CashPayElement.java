package org.shoestore.infra.pay;

import org.shoestore.domain.model.pay.PayElement;
import org.shoestore.domain.model.pay.PayMethod;

public class CashPayElement extends PayElement {

    public CashPayElement(Long id, Long payAmount) {
        super(id, PayMethod.CASH, payAmount);
    }

    public CashPayElement(Long payAmount) {

        super(PayMethod.CASH, payAmount);
    }

    public static CashPayElement init(Long payAmount) {

        return new CashPayElement(payAmount);
    }


    @Override
    public void processPayment() {

    }

    @Override
    public void cancelPayment() {

    }
}
