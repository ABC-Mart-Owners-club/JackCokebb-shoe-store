package org.shoestore.application;

import java.util.Map;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.domain.model.stock.StockRepository;
import org.shoestore.domain.model.stock.Stock;
import org.shoestore.interfaces.product.dto.StockAddRequest;
import org.shoestore.interfaces.product.dto.StockAddResponse;
import org.shoestore.interfaces.product.dto.StockFindRequest;
import org.shoestore.interfaces.product.dto.StockFindResponse;

public class StockService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public StockService(ProductRepository productRepository, StockRepository stockRepository) {

        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    public StockAddResponse orderStocks(StockAddRequest request) {

        Map<Long, Stock> stocksMap = stockRepository.findStocksByProductIdsAsMap(
            request.getProductIdsAsSet());
        request.getStockElements()
            .forEach(req -> {
                Stock stock = stocksMap.get(req.getProductId());
                stocksMap.put(stock.getProductId(), stock.restock(req.getQuantity()));
            });

        return new StockAddResponse(!stockRepository.saveAll(stocksMap.values().stream().toList()).isEmpty());
    }

    public StockFindResponse findStocks(StockFindRequest request) {

        Product product = productRepository.findById(request.productId());
        Stock stock = stockRepository.findByProductId(request.productId());
        return new StockFindResponse(product, stock);
    }
}
