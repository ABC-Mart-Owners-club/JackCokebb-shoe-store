package org.shoestore.domain.model.pay;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

public class PayElement {

    private Long id;

    private PayMethod payMethod;

    private IssuingBank issuingBank;

    private Long payAmount;

    public PayElement(Long id, PayMethod payMethod, IssuingBank issuingBank, Long payAmount) {
        this.id = id;
        this.payMethod = payMethod;
        this.issuingBank = issuingBank;
        this.payAmount = payAmount;
    }

    private PayElement(PayMethod payMethod, IssuingBank issuingBank, Long payAmount) {
        this.id = getNewId();
        this.payMethod = payMethod;
        this.issuingBank = issuingBank;
        this.payAmount = payAmount;
    }

    public static PayElement init(PayMethod payMethod, IssuingBank issuingBank,
        Long payAmount) {

        if (PayMethod.CARD == payMethod && issuingBank == null) {

            throw new IllegalArgumentException("Card's IssuingBank is required");
        }

        if (PayMethod.CARD == payMethod) {

            return new PayElement(PayMethod.CARD, issuingBank, payAmount);

        } else {

            return new PayElement(PayMethod.CASH, null, payAmount);
        }

    }

    public Long getPayAmount() {

        return payAmount;
    }

    private static Long getNewId() {

        return UUID.randomUUID().getMostSignificantBits() - LocalDateTime.now().toInstant(
            ZoneOffset.UTC).toEpochMilli();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PayElement that = (PayElement) o;
        return Objects.equals(id, that.id) && payMethod == that.payMethod
            && issuingBank == that.issuingBank && Objects.equals(payAmount, that.payAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, payMethod, issuingBank, payAmount);
    }
}
