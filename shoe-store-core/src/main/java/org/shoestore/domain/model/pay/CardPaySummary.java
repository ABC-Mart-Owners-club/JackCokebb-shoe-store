package org.shoestore.domain.model.pay;

import java.util.List;
import org.shoestore.domain.model.pay.Payment.IssuingBank;
import org.shoestore.domain.model.pay.Payment.PayElement;

public class CardPaySummary {

    private IssuingBank issuingBank;

    private List<PayElement> cardPayElements;

    public CardPaySummary(IssuingBank issuingBank, List<PayElement> cardPayElements) {
        this.issuingBank = issuingBank;
        this.cardPayElements = cardPayElements;
    }

    public IssuingBank getIssuingBank() {

        return issuingBank;
    }

    public List<PayElement> getCardPayElements() {

        return cardPayElements;
    }

    public Long getTotalAmount() {

        return cardPayElements.stream().mapToLong(PayElement::getPayAmount).sum();
    }
}
