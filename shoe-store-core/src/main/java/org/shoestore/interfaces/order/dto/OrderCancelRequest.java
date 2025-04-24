package org.shoestore.interfaces.order.dto;

public class OrderCancelRequest {

    private Long orderId;

    public OrderCancelRequest(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {

        return orderId;
    }
}