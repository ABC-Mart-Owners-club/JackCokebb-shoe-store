package org.shoestore.interfaces.order.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.shoestore.domain.model.order.Order;
import org.shoestore.domain.model.order.OrderElement;

public class OrderCreateRequestDto {

    private Long customerId;

    private List<OrderElementCreateDto> orderElements;

    public Order toOrderDomain() {
        return Order.init(
            customerId,
            Optional.ofNullable(orderElements).orElseGet(ArrayList::new)
                .stream()
                .collect(
                    Collectors.toMap(
                        OrderElementCreateDto::getProductId,
                        OrderElementCreateDto::toOrderElementDomain
                    )
                )
        );
    }

    public static class OrderElementCreateDto {

        private Long productId;

        private Long quantity;

        public Long getProductId() {

            return productId;
        }

        public OrderElement toOrderElementDomain() {

            return OrderElement.init(productId, quantity);
        }
    }
}
