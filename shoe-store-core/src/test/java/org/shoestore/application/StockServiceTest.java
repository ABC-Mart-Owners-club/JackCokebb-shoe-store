package org.shoestore.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.interfaces.product.dto.StockAddRequest;
import org.shoestore.interfaces.product.dto.StockAddRequest.StockElementDto;
import org.shoestore.interfaces.product.dto.StockAddResponse;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    StockService stockService;

    private final static Long PRODUCT1_ID = 1L;
    private final static Long PRODUCT2_ID = 2L;
    private final static Long PRODUCT3_ID = 3L;

    private final static Long PRODUCT1_STOCK_QUANTITY = 10L;
    private final static Long PRODUCT2_STOCK_QUANTITY = 20L;
    private final static Long PRODUCT3_STOCK_QUANTITY = 30L;

    private final static Long PRODUCT1_QUANTITY = 1L;
    private final static Long PRODUCT2_QUANTITY = 2L;
    private final static Long PRODUCT3_QUANTITY = 3L;

    private final static String PRODUCT1_NAME = "Product 1";
    private final static String PRODUCT2_NAME = "Product 2";
    private final static String PRODUCT3_NAME = "Product 3";

    private final static Long PRODUCT1_PRICE = 123L;
    private final static Long PRODUCT2_PRICE = 456L;
    private final static Long PRODUCT3_PRICE = 789L;

    //TODO: @Test
    public void orderStocksTest() {

        // given
        StockElementDto stockElementDto1 = new StockElementDto(PRODUCT1_ID, PRODUCT1_QUANTITY);
        StockElementDto stockElementDto2 = new StockElementDto(PRODUCT2_ID, PRODUCT2_QUANTITY);
        StockElementDto stockElementDto3 = new StockElementDto(PRODUCT3_ID, PRODUCT3_QUANTITY);

        StockAddRequest stockAddRequest = new StockAddRequest(
            List.of(stockElementDto1, stockElementDto2, stockElementDto3));

        Product product1 = new Product(PRODUCT1_ID, PRODUCT1_NAME, PRODUCT1_PRICE);
        Product product2 = new Product(PRODUCT2_ID, PRODUCT2_NAME, PRODUCT2_PRICE);
        Product product3 = new Product(PRODUCT3_ID, PRODUCT3_NAME, PRODUCT3_PRICE);

        when(productRepository.findAllByIdsAsMap(Set.of(PRODUCT1_ID, PRODUCT2_ID, PRODUCT3_ID)))
            .thenReturn(Map.of(PRODUCT1_ID, product1, PRODUCT2_ID, product2, PRODUCT3_ID, product3));
        when(productRepository.saveAll(anyList())).thenAnswer(method -> method.getArguments()[0]);

        // when
        StockAddResponse stockAddResponse = stockService.orderStocks(stockAddRequest);

        // then
        /* TODO:
        assertTrue(stockAddResponse.success());
        assertEquals(product1.getStock().getTotalQuantity(), PRODUCT1_STOCK_QUANTITY + PRODUCT1_QUANTITY);
        assertEquals(product2.getStock().getTotalQuantity(), PRODUCT2_STOCK_QUANTITY + PRODUCT2_QUANTITY);
        assertEquals(product3.getStock().getTotalQuantity(), PRODUCT3_STOCK_QUANTITY + PRODUCT3_QUANTITY);
    }

    public StockAddResponse orderStocks(StockAddRequest request) {

        Map<Long, Product> productMap = productRepository.findAllByIdsAsMap(
            request.getProductIdsAsSet());
        request.getStockElements().forEach(element -> productMap.get(element.getProductId()).addStock(element.getQuantity()));

        return new StockAddResponse(!productRepository.saveAll(productMap.values().stream().toList()).isEmpty()); */
    }
}
