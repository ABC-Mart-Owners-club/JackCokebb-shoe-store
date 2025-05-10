package org.shoestore.domain.model.pay;

import java.util.List;

public class CardPaySummary {

    private PayMethod payMethod;

    private List<PayElement> cardPayElements;

    public CardPaySummary(PayMethod payMethod, List<PayElement> cardPayElements) {
        this.payMethod = payMethod;
        this.cardPayElements = cardPayElements;
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public List<PayElement> getCardPayElements() {

        return cardPayElements;
    }

    public Long getTotalAmount() {

        return cardPayElements.stream().mapToLong(PayElement::getPayAmount).sum();
    }
}
