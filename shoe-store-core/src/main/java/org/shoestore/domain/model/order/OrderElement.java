package org.shoestore.domain.model.order;

import java.util.Objects;
import org.shoestore.domain.model.product.Product;

public class OrderElement {

    private Long productId;

    private Long quantity;

    private Long priceForEach;

    private boolean isCanceled;

    public OrderElement(Long productId, Long priceForEach, Long quantity, boolean isCanceled) {

        this.productId = productId;
        this.priceForEach = priceForEach;
        this.quantity = quantity;
        this.isCanceled = isCanceled;
    }

    public static OrderElement init(Product product, Long quantity) {

        return new OrderElement(product.getId(), product.getPrice(), quantity, false);
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
