package org.shoestore.interfaces.order.dto;

import java.util.List;
import org.shoestore.interfaces.pay.dto.PayElementDto;

public class OrderPartialCancelRequest {

    private Long orderId;

    private List<Long> productIds;

    private List<PayElementDto> payElements;

    public OrderPartialCancelRequest(Long orderId, List<Long> productIds,
        List<PayElementDto> payElements) {
        this.orderId = orderId;
        this.productIds = productIds;
        this.payElements = payElements;
    }

    public Long getOrderId() {

        return orderId;
    }

    public List<Long> getProductIds() {

        return productIds;
    }

    public List<PayElementDto> getPayElements() {

        return payElements;
    }
}