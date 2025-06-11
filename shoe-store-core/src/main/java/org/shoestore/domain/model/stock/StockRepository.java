package org.shoestore.domain.model.stock;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StockRepository {

    Map<Long, Stock> findStocksByProductIdsAsMap(Set<Long> productIds);

    Stock findByProductId(Long productId);

    List<StockElement> saveAllStockElement(List<StockElement> stockElements);

    List<StockHistory> saveAllStockHistory(List<StockHistory> stockElements);

    default Stock save(Stock stock) {

        return new Stock(stock.getProductId(), saveAllStockElement(stock.getStockElements()),
            saveAllStockHistory(stock.getStockHistories()));
    }

    default List<Stock> saveAll(List<Stock> stock) {

        return stock.stream().map(this::save).toList();
    }
}
