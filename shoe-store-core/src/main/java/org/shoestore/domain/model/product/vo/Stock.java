package org.shoestore.domain.model.product.vo;

public class Stock {

    private final Long quantity;

    public Stock(Long quantity) {

        this.quantity = quantity;
    }

    public Long getQuantity() {

        return quantity;
    }

    public void hasEnoughStock(Long requestedQuantity) {

        if (this.quantity < requestedQuantity) {

            throw new IllegalArgumentException("Not enough stock");
        }
    }

    public Stock minusStockIfEnoughOrElseThrow(Long requestedQuantity) {

        hasEnoughStock(requestedQuantity);

        return new Stock(quantity - requestedQuantity);
    }



    public Stock addStock(Long requestedQuantity) {

        return new Stock(quantity + requestedQuantity);
    }
}
