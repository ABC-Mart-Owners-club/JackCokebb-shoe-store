package org.shoestore.domain.model.product;

import org.shoestore.domain.model.stock.StockElement;

public class Product {

    private Long id;

    private String name;

    private Long price;

    public static final double _30_DAYS_OLD_PRODUCT_DISCOUNTED_RATE = (double) 50 / 100;
    public static final double _7_DAYS_OLD_PRODUCT_DISCOUNTED_RATE = (double) 30 / 100;

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

    public Long getOriginPrice() {

        return price;
    }

    public Long getActualPrice(StockElement stockElement) {

        if (stockElement.isStocked30DaysAgo()) {
            return getOriginPrice() - (long) Math.ceil(Double.valueOf(price) * (_30_DAYS_OLD_PRODUCT_DISCOUNTED_RATE));
        } else if (stockElement.isStocked7DaysAgo()) {
            return getOriginPrice() - (long) Math.ceil(Double.valueOf(price) * (_7_DAYS_OLD_PRODUCT_DISCOUNTED_RATE));
        } else {
            return getOriginPrice();
        }
    }
}
