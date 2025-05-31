package org.shoestore.interfaces.product.dto;

import java.util.Objects;
import org.shoestore.domain.model.product.Product;

public class ProductPriceResponse {

    private final String name;

    private final Long price;

    public ProductPriceResponse(String name, Long price) {

        this.name = name;
        this.price = price;
    }

    public static ProductPriceResponse from(Product product) {

        return new ProductPriceResponse(product.getName(), product.getOriginPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductPriceResponse that = (ProductPriceResponse) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
