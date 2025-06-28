package org.shoestore.interfaces.product.dto;


import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.stock.Stock;

public record StockFindResponse(Long productId, String productName, Long quantity) {

    public StockFindResponse(Product product, Stock stock) {

        this(product.getId(), product.getName(), stock.getTotalQuantity());
    }
}
