package org.shoestore.interfaces.product.dto;

import org.shoestore.domain.model.product.Product;

public record StockFindResponse(Long productId, String productName, Long quantity) {

    public StockFindResponse(Product product) {

        this(product.getId(), product.getName(), product.getStock().getQuantity());
    }
}
