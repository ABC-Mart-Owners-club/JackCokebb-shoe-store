package org.shoestore.domain.model.stock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class StockHistory {

    private Long id;

    private Long orderId;

    private Long stockElementId;

    private Long stockOutQuantity;

    private LocalDateTime createdAt;

    public static final long NONE_ORDER_ID = -1;

    public StockHistory(Long id, Long orderId, Long stockElementId, Long stockInQuantity,
        Long stockOutQuantity, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.stockElementId = stockElementId;
        this.stockOutQuantity = stockOutQuantity;
        this.createdAt = createdAt;
    }

    public static StockHistory stockInByOrder(Long orderId, Long stockElementId, Long stockInQuantity) {

        return new StockHistory(getNewId(), orderId, stockElementId, stockInQuantity, 0L, LocalDateTime.now());
    }

    public static StockHistory stockOutByOrder(Long orderId, Long stockElementId, Long stockOutQuantity) {

        return new StockHistory(getNewId(), orderId, stockElementId, 0L, stockOutQuantity, LocalDateTime.now());
    }

    public static StockHistory stockIn(Long stockElementId, Long stockInQuantity) {

        return new StockHistory(getNewId(), NONE_ORDER_ID, stockElementId, stockInQuantity, 0L, LocalDateTime.now());
    }

    public static StockHistory stockOut(Long stockElementId, Long stockOutQuantity) {

        return new StockHistory(getNewId(), NONE_ORDER_ID, stockElementId, 0L, stockOutQuantity, LocalDateTime.now());
    }

    private static Long getNewId() {

        return UUID.randomUUID().getMostSignificantBits() - LocalDateTime.now()
            .toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getStockElementId() {
        return stockElementId;
    }

    public Long getStockOutQuantity() {
        return stockOutQuantity;
    }
}
