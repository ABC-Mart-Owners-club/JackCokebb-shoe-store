package org.shoestore.domain.model.order;

import java.util.Objects;

public class OrderElement {

    private Long id;

    private Long orderId;

    private Long productId;

    private Long quantity;

    private boolean isCanceled;

    public OrderElement(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.isCanceled = false;
    }

    public static OrderElement init(Long productId, Long quantity) {

        return new OrderElement(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void cancel() {

        this.isCanceled = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderElement that = (OrderElement) o;
        return isCanceled == that.isCanceled && Objects.equals(id, that.id)
            && Objects.equals(orderId, that.orderId) && Objects.equals(productId,
            that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, productId, quantity, isCanceled);
    }
}
