package org.shoestore.interfaces.order.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderCreateRequest {

    private Long customerId;

    private List<OrderElementCreateDto> orderElements;

    public OrderCreateRequest(Long customerId, List<OrderElementCreateDto> orderElements) {
        this.customerId = customerId;
        this.orderElements = orderElements;
    }

    public Long getCustomerId() {

        return customerId;
    }

    public List<OrderElementCreateDto> getOrderElements() {

        return orderElements;
    }

    public Set<Long> getOrderedProductIds() {

        return orderElements.stream()
            .map(OrderElementCreateDto::getProductId)
            .collect(Collectors.toSet());
    }

    public static class OrderElementCreateDto {

        private Long productId;

        private Long quantity;

        public OrderElementCreateDto(Long productId, Long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {

            return productId;
        }

        public Long getQuantity() {

            return quantity;
        }
    }
}
