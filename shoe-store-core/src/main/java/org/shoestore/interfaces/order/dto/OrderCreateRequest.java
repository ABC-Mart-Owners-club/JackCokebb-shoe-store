package org.shoestore.interfaces.order.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.shoestore.domain.model.pay.Coupon;

public class OrderCreateRequest {

    private Long customerId;

    private Coupon coupon;

    private List<OrderElementCreateDto> orderElements;

    public OrderCreateRequest(Long customerId, Coupon coupon, List<OrderElementCreateDto> orderElements) {
        this.customerId = customerId;
        this.coupon = coupon;
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

    public Coupon getCoupon() {

        return coupon;
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
