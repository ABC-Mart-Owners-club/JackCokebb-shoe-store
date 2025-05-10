package org.shoestore.infra.pay;

import java.util.EnumMap;
import java.util.function.Function;
import org.shoestore.domain.model.pay.PayElement;
import org.shoestore.domain.model.pay.PayMethod;

public class PayElementRegistry {

    private EnumMap<PayMethod, Function<Long, PayElement>> payElementMap;

    public PayElementRegistry() {

        this.payElementMap = new EnumMap<>(PayMethod.class);

        this.payElementMap.put(PayMethod.CASH, CashPayElement::init);
        this.payElementMap.put(PayMethod.NAHA_CARD, NaHaCardPayElement::init);
        this.payElementMap.put(PayMethod.DAIHYUN_CARD, DaiHyunCardPayElement::init);
    }

    public Function<Long, PayElement> get(PayMethod method) {

        return payElementMap.get(method);
    }
}
