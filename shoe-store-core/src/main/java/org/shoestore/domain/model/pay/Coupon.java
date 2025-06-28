package org.shoestore.domain.model.pay;

public enum Coupon {

    DISCOUNT_10_PERCENT(0.1),
    NONE(0.0)
    ;

    private double discountRate;

    Coupon(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
