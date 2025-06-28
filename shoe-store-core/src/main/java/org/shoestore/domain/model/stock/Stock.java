package org.shoestore.domain.model.stock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Stock {

    // @Id
    private final Long productId;

    private final List<StockElement> stockElements;

    private final List<StockHistory> stockHistories;

    public Stock(Long productId, List<StockElement> stockElements) {
        this.productId = productId;
        this.stockElements = stockElements;
        this.stockHistories = new ArrayList<>(); // 생성되는 VO가 처리하는 작업의 history를 저장할 목적.
    }

    public Stock(Long productId, List<StockElement> stockElements, List<StockHistory> stockHistories) {
        this.productId = productId;
        this.stockElements = stockElements;
        this.stockHistories = stockHistories;
    }

    public Long getProductId() {

        return productId;
    }

    public List<StockElement> getStockElements() {

        return Optional.ofNullable(this.stockElements).orElseGet(List::of);
    }

    public List<StockHistory> getStockHistories() {

        return stockHistories;
    }

    public Long getTotalQuantity() {

        return getStockElements()
            .stream()
            .mapToLong(StockElement::getQuantity)
            .sum();
    }

    public void hasEnoughStock(Long requestedQuantity) {

        if (getTotalQuantity() < requestedQuantity) {

            throw new IllegalArgumentException("Not enough stock");
        }
    }

    public Stock minusStockAndLeftHistoryIfEnoughOrElseThrow(Long orderId, Long requestedQuantity) {

        hasEnoughStock(requestedQuantity);

        this.stockElements.sort(Comparator.comparing(StockElement::getStockedAt));

        for (StockElement element : this.stockElements) {
            if (requestedQuantity < 0) {
                break;
            }

            Long currQuantity = element.getQuantity();

            if (currQuantity < 0) {
                continue;
            }

            if (currQuantity > requestedQuantity) {

                element.minusQuantity(requestedQuantity);
                this.stockHistories.add(StockHistory.stockOutByOrder(orderId, element.getId(), requestedQuantity));

            } else {

                element.minusQuantity(currQuantity);
                requestedQuantity -= currQuantity;
                this.stockHistories.add(StockHistory.stockOutByOrder(orderId, element.getId(), currQuantity));
            }
        }

        return new Stock(this.productId, this.stockElements, this.stockHistories);

    }

    public Stock restockByOrder(Long orderId, Long quantity) {

        List<StockHistory> historyByOrderId = findHistoryByOrderId(orderId);
        historyByOrderId.forEach(history -> {
            StockElement element = findElementById(history.getStockElementId());
            element.restock(history.getStockOutQuantity());
            this.stockHistories.add(StockHistory.stockInByOrder(orderId, element.getId(), quantity));
        });

        return new Stock(this.productId, getStockElements(), getStockHistories());
    }

    public Stock restock(Long quantity) {

        StockElement newElement = StockElement.init(productId, quantity);
        getStockElements().add(newElement);
        getStockHistories().add(StockHistory.stockIn(newElement.getId(), quantity));

        return new Stock(this.productId, getStockElements(), getStockHistories());
    }

    public StockElement findElementById(Long stockElementId) {

        return getStockElements().stream()
            .filter(e -> Objects.equals(stockElementId, e.getId()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Stock element not found"));
    }

    public List<StockHistory> findHistoryByOrderId(Long orderId) {

        return Optional.ofNullable(this.stockHistories).orElseGet(List::of)
            .stream()
            .filter(history -> Objects.equals(history.getOrderId(), orderId))
            .toList();
    }

    public StockElement findOldestElement() {

        hasEnoughStock(1L);

        this.stockElements.sort(Comparator.comparing(StockElement::getStockedAt));

        return this.stockElements.getFirst();
    }
}
