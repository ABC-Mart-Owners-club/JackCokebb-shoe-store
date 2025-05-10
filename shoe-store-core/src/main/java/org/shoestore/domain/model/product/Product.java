package org.shoestore.domain.model.product;

import org.shoestore.domain.model.product.vo.Stock;

public class Product {

    private Long id;

    private String name;

    private Long price;

    private Stock stock;

    public Product(Long id, String name, Long price, Long stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = new Stock(stockQuantity);
    }

    public Long getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public Long getPrice() {

        return price;
    }

    public Stock getStock() {

        return stock;
    }

    public void minusStockIfEnoughOrElseThrow(Long requestedQuantity) {

        this.stock = stock.minusStockIfEnoughOrElseThrow(requestedQuantity);
    }

    public void addStock(Long requestedQuantity) {

        this.stock = stock.addStock(requestedQuantity);
    }
}
