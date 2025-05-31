package org.shoestore.domain.model.pay;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.shoestore.domain.model.order.OrderElement;

public class Payment {

    private Long id;

    private Long requestedAmount;

    private List<PayElement> payElements;

    private PayStatus payStatus;

    private Coupon couponApplied;

    private LocalDateTime createdAt;

    private LocalDateTime paidAt;

    public Long getId() {

        return id;
    }

    public PayStatus getPayStatus() {

        return payStatus;
    }

    public Coupon getCouponApplied() {

        return couponApplied;
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

    public Payment(Long id, PayStatus payStatus, List<PayElement> payElements, Long requestedAmount,
        Coupon couponApplied) {
        this.id = id;
        this.payStatus = payStatus;
        this.requestedAmount = requestedAmount;
        this.payElements = payElements;
        this.couponApplied = couponApplied;
        this.createdAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    public Payment(Long id, Long requestedAmount, PayStatus payStatus, List<PayElement> payElements,
        Coupon couponApplied,
        LocalDateTime createdAt, LocalDateTime paidAt) {
        this.id = id;
        this.payStatus = payStatus;
        this.requestedAmount = requestedAmount;
        this.payElements = payElements;
        this.couponApplied = couponApplied;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
    }

    public static Payment init(Long requestedAmount, Coupon coupon) {

        return new Payment(getNewId(), PayStatus.REQUESTED, new ArrayList<>(),
            (long) Math.ceil(requestedAmount - (requestedAmount * coupon.getDiscountRate())),
            coupon);
    }

    public static Payment init(List<OrderElement> orderElements, Coupon coupon) {

        long requestedAmount = orderElements.stream()
            .mapToLong(e -> e.getPriceForEach() * e.getQuantity())
            .sum();

        return new Payment(getNewId(), PayStatus.REQUESTED, new ArrayList<>(),
            (long) Math.ceil(requestedAmount - (requestedAmount * coupon.getDiscountRate())),
            coupon);
    }

    private static Long getNewId() {

        return UUID.randomUUID().getMostSignificantBits() - LocalDateTime.now()
            .toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public void pay(List<PayElement> payElements) {

        if (this.payElements == null) {

            this.payElements = new ArrayList<>();
        }

        long elementSum = payElements.stream().mapToLong(PayElement::getPayAmount).sum();
        if (elementSum != requestedAmount) {

            throw new IllegalArgumentException("Pay amount mismatch");
        }

        payElements.forEach(PayElement::processPayment);

        this.payElements.addAll(payElements);
        this.payStatus = PayStatus.PAID;
        this.paidAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    public void cancel() {

        payElements.forEach(PayElement::cancelPayment);
        this.payStatus = PayStatus.CANCELED;
    }

    public boolean isAllPaid() {

        return payElements.stream().mapToLong(PayElement::getPayAmount).sum() == requestedAmount;
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
