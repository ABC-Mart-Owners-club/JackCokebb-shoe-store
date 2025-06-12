package org.shoestore.domain.model.stock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class StockElement {

    private Long id;

    private Long productId;

    private Long quantity;

    private LocalDateTime stockedAt;

    public StockElement(Long id, Long productId, Long quantity, LocalDateTime stockedAt) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.stockedAt = stockedAt;
    }

    public static StockElement init(Long productId, Long quantity) {

        return new StockElement(getNewId(), productId, quantity, LocalDateTime.now(ZoneOffset.UTC));
    }

    private static Long getNewId() {

        return UUID.randomUUID().getMostSignificantBits() - LocalDateTime.now()
            .toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public LocalDateTime getStockedAt() {
        return stockedAt;
    }

    public void minusQuantity(Long quantity) {
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("Not enough stock elements");
        }

        this.quantity = this.quantity - quantity;
    }

    public void restock(Long quantity) {

        this.quantity = this.quantity + quantity;
    }

    public boolean isStocked30DaysAgo() {

        return stockedAt.isBefore(LocalDateTime.now().minusDays(30));
    }

    public boolean isStocked7DaysAgo() {

        return stockedAt.isBefore(LocalDateTime.now().minusDays(7));
    }
}
