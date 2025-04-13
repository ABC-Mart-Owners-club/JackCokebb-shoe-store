package org.shoestore.interfaces.product.dto;

import org.shoestore.domain.model.product.Product;

public class ProductPriceResponse {

    private final String name;

    private final Long price;

    public ProductPriceResponse(String name, Long price) {

        this.name = name;
        this.price = price;
    }

    public static ProductPriceResponse from(Product product) {

        return new ProductPriceResponse(product.getName(), product.getPrice());
    }
}
