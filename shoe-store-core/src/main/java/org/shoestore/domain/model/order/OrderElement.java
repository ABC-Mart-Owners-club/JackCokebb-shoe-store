package org.shoestore.domain.model.order;

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
}
