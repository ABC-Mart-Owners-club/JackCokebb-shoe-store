package org.shoestore.domain.model.product;

import org.shoestore.domain.model.stock.vo.Stock;

public class Product {

    private Long id;

    private String name;

    private Long price;

    public Product(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
}
