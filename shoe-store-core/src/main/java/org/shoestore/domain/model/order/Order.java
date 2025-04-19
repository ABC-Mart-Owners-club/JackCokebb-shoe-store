package org.shoestore.domain.model.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.shoestore.domain.model.product.ProductPriceInfo;

public class Order {

    private Long id;

    private Long customerId;

    private Map<Long, OrderElement> orderElements;

    private Long totalPrice;

    private Order(Long customerId, Map<Long, OrderElement> orderElements) {

        this.customerId = customerId;
        this.orderElements = orderElements;
    }

    public static Order init(Long customerId, Map<Long, OrderElement> orderElements) {

        return new Order(customerId, orderElements);
    }

    public Order(Long id, Long customerId, Map<Long, OrderElement> orderElements, Long totalPrice) {

        this.id = id;
        this.customerId = customerId;
        this.orderElements = orderElements;
        this.totalPrice = totalPrice;
    }

    public Set<Long> getProductIds() {

        return Optional.ofNullable(orderElements)
            .orElseGet(HashMap::new)
            .keySet();
    }

    public Long getCustomerId() {

        return customerId;
    }

    public void calculateTotalPrice(Map<Long, ProductPriceInfo> priceInfoMap) {

        this.totalPrice = orderElements.keySet().stream()
            .map(priceInfoMap::get)
            .mapToLong(ProductPriceInfo::getPrice)
            .sum();
    }

    public void cancelAll() {

        orderElements.values()
            .forEach(OrderElement::cancel);
    }

    public void cancel(List<Long> orderElementIds) {

        orderElementIds.stream()
            .filter(id -> orderElements.containsKey(id))
            .forEach(id -> orderElements.get(id).cancel());
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
            order.customerId) && Objects.equals(orderElements, order.orderElements)
            && Objects.equals(totalPrice, order.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, orderElements, totalPrice);
    }
}
