package org.shoestore.interfaces.order.dto;

import java.util.List;

public class OrderPartialCancelRequest {

    private Long orderId;

    private List<Long> orderElementIds;

    public Long getOrderId() {

        return orderId;
    }

    public List<Long> getOrderElementIds() {

        return orderElementIds;
    }
}