package org.shoestore.domain.model.stock;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StockRepository {

    Map<Long, Stock> findStocksByProductIdsAsMap(Set<Long> productIds);

    Stock findByProductId(Long productId);

    Stock saveStock(Stock stock);

    List<StockElement> saveAllStockElement(List<StockElement> stockElements);

    List<StockHistory> saveAllStockHistory(List<StockHistory> stockElements);

    default List<Stock> saveAll(List<Stock> stock) {

        return stock.stream().map(this::saveStock).toList();
    }
}
