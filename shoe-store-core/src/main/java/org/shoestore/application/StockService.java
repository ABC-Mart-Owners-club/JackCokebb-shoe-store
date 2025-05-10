package org.shoestore.application;

import java.util.Map;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.interfaces.product.dto.StockAddRequest;
import org.shoestore.interfaces.product.dto.StockAddResponse;
import org.shoestore.interfaces.product.dto.StockFindRequest;
import org.shoestore.interfaces.product.dto.StockFindResponse;

public class StockService {

    private final ProductRepository productRepository;

    public StockService(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    public StockAddResponse orderStocks(StockAddRequest request) {

        Map<Long, Product> productMap = productRepository.findAllByIdsAsMap(
            request.getProductIdsAsSet());
        request.getStockElements().forEach(element -> productMap.get(element.getProductId()).addStock(element.getQuantity()));

        return new StockAddResponse(!productRepository.saveAll(productMap.values().stream().toList()).isEmpty());
    }

    public StockFindResponse findStocks(StockFindRequest request) {

        return new StockFindResponse(productRepository.findById(request.productId()));
    }
}
