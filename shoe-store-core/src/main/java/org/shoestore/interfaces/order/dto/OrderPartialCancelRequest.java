package org.shoestore.interfaces.order.dto;

import java.util.List;

public class OrderPartialCancelRequest {

    private Long orderId;

    private List<Long> productIds;

    public OrderPartialCancelRequest(Long orderId, List<Long> productIds) {
        this.orderId = orderId;
        this.productIds = productIds;
    }

    public Long getOrderId() {

        return orderId;
    }

    public List<Long> getProductIds() {

        return productIds;
    }
}