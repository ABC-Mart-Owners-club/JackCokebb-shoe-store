package org.shoestore.domain.model.order;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.stock.StockElement;
import org.shoestore.domain.model.stock.Stock;

public class OrderElement {

    private Long productId;

    private Long quantity;

    private Long priceForEach;

    private boolean isCanceled;

    public OrderElement(Long productId, Long priceForEach, Long quantity,
        boolean isCanceled) {

        this.productId = productId;
        this.priceForEach = priceForEach;
        this.quantity = quantity;
        this.isCanceled = isCanceled;
    }

    public static List<OrderElement> init(Product product, Stock stock, Long quantity) {
        stock.hasEnoughStock(quantity);
        HashMap<Long, OrderElement> elementMap = new HashMap<>();

        // stock 입고 날짜별 가격 계산을 위해 stock element를 stock element id로 관리
        while (quantity > 0) {

            StockElement oldestElement = stock.findOldestElement();
            OrderElement orderElement = elementMap.getOrDefault(
                oldestElement.getId(),
                new OrderElement(oldestElement.getProductId(), product.getActualPrice(oldestElement), 0L, false)
            );

            elementMap.put(oldestElement.getId(), orderElement.increaseQuantityAndReturn(1L));
            quantity--;
        }

        return elementMap.values().stream().collect(Collectors.toList());
    }

    public Long getProductId() {
        return productId;
    }

    public Long getPriceForEach() {

        return priceForEach;
    }

    public Long getQuantity() {

        return quantity;
    }

    public Long getTotalPrice() {

        return quantity * priceForEach;
    }

    public void cancel() {

        this.isCanceled = true;
    }

    public boolean isCanceled() {

        return isCanceled;
    }

    public OrderElement increaseQuantityAndReturn(Long count) {
        if (this.quantity == null) {
            this.quantity = count;
        }

        this.quantity += count;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderElement that = (OrderElement) o;
        return isCanceled == that.isCanceled && Objects.equals(productId, that.productId)
            && Objects.equals(quantity, that.quantity) && Objects.equals(
            priceForEach, that.priceForEach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity, priceForEach, isCanceled);
    }
}
