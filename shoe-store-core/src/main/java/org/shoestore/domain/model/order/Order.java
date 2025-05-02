package org.shoestore.domain.model.order;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Order {

    private Long id;

    private Long customerId;

    private Long payId;

    // key: productId
    private Map<Long, OrderElement> orderElements;

    private OrderStatus status;

    public Order(Long id, Long customerId, Long payId, Map<Long, OrderElement> orderElements) {

        this.id = id;
        this.customerId = customerId;
        this.payId = payId;
        this.orderElements = orderElements;
        this.status = OrderStatus.REQUESTED;
    }

    public static Order init(Long customerId, Long payId, Map<Long, OrderElement> orderElements) {

        return new Order(getNewId(), customerId, payId, orderElements);
    }

    public Set<Long> getProductIds() {

        return Optional.ofNullable(orderElements)
            .orElseGet(HashMap::new)
            .keySet();
    }

    public Long getCustomerId() {

        return customerId;
    }

    public Long getTotalPrice() {

        return this.orderElements.values().stream()
            .mapToLong(OrderElement::getPriceForEach)
            .sum();
    }

    public void cancelAll() {

        orderElements.values()
            .forEach(OrderElement::cancel);
        status = OrderStatus.CANCELED;
    }

    public void cancel(List<Long> productIds) {

        productIds.stream()
            .filter(id -> orderElements.containsKey(id))
            .forEach(id -> orderElements.get(id).cancel());
    }

    private static Long getNewId() {

        return UUID.randomUUID().getMostSignificantBits() - LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;

        return Objects.equals(id, order.id) && Objects.equals(customerId,
            order.customerId) && Objects.equals(orderElements, order.orderElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, orderElements);
    }

    public void updateOrderStatusByPaidStatus(boolean allPaid) {

        if (allPaid) {
            status = OrderStatus.COMPLETED;
        }
    }
}
