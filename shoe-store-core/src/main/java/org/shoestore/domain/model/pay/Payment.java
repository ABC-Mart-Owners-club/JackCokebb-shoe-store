package org.shoestore.domain.model.pay;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Payment {

    private Long id;

    private Long requestedAmount;

    private List<PayElement> payElements;

    private LocalDateTime createdAt;

    private LocalDateTime paidAt;

    public Long getId() {

        return id;
    }

    public List<PayElement> getPayElements() {

        return payElements;
    }

    public LocalDateTime getPaidAt() {

        return paidAt;
    }

    public Long getRequestedAmount() {

        return requestedAmount;
    }

    public LocalDateTime getCreatedAt() {

        return createdAt;
    }

    private Payment(Long id, List<PayElement> payElements, Long requestedAmount) {
        this.id = id;
        this.requestedAmount = requestedAmount;
        this.payElements = payElements;
        this.createdAt = LocalDateTime.now();
    }

    public Payment(Long id, Long requestedAmount, List<PayElement> payElements,
        LocalDateTime createdAt, LocalDateTime paidAt) {
        this.id = id;
        this.requestedAmount = requestedAmount;
        this.payElements = payElements;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
    }

    public static Payment init(Long requestedAmount) {

        return new Payment(getNewId(), new ArrayList<>(), requestedAmount);
    }

    private static Long getNewId() {

        return UUID.randomUUID().getMostSignificantBits() - LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public void pay(List<PayElement> payElements) {

        if (this.payElements == null) {

            this.payElements = new ArrayList<>();
        }

        long elementSum = payElements.stream().mapToLong(PayElement::getPayAmount).sum();
        if (elementSum != requestedAmount) {

            throw new IllegalArgumentException("Pay amount mismatch");
        }

        this.payElements.addAll(payElements);
        this.paidAt = LocalDateTime.now();
    }

    public static class PayElement {

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

            return UUID.randomUUID().getMostSignificantBits() - LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
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

    public enum PayMethod {
        CARD,
        CASH,
        ;
    }

    public enum IssuingBank {
        DAIHYUN,
        NAHA,
        MINKOOK,
        RIWOO,
        ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) && Objects.equals(requestedAmount,
            payment.requestedAmount) && Objects.equals(payElements, payment.payElements)
            && Objects.equals(createdAt, payment.createdAt) && Objects.equals(
            paidAt, payment.paidAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestedAmount, payElements, createdAt, paidAt);
    }
}
