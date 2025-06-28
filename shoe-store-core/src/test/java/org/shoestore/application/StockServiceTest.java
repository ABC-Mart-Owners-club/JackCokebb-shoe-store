package org.shoestore.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.domain.model.stock.Stock;
import org.shoestore.domain.model.stock.StockElement;
import org.shoestore.domain.model.stock.StockRepository;
import org.shoestore.interfaces.product.dto.StockAddRequest;
import org.shoestore.interfaces.product.dto.StockAddRequest.StockElementDto;
import org.shoestore.interfaces.product.dto.StockAddResponse;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    StockRepository stockRepository;

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

    @Test
    public void orderStocksTest() {

        // given
        StockElementDto stockElementDto1 = new StockElementDto(PRODUCT1_ID, PRODUCT1_QUANTITY);
        StockElementDto stockElementDto2 = new StockElementDto(PRODUCT2_ID, PRODUCT2_QUANTITY);
        StockElementDto stockElementDto3 = new StockElementDto(PRODUCT3_ID, PRODUCT3_QUANTITY);

        StockElement stockElement1 = StockElement.init(PRODUCT1_ID, PRODUCT1_STOCK_QUANTITY);
        StockElement stockElement2 = StockElement.init(PRODUCT2_ID, PRODUCT2_STOCK_QUANTITY);
        StockElement stockElement3 = StockElement.init(PRODUCT3_ID, PRODUCT3_STOCK_QUANTITY);

        Stock stock1 = new Stock(PRODUCT1_ID, new ArrayList<>(List.of(stockElement1)));
        Stock stock2 = new Stock(PRODUCT2_ID, new ArrayList<>(List.of(stockElement2)));
        Stock stock3 = new Stock(PRODUCT3_ID, new ArrayList<>(List.of(stockElement3)));

        StockAddRequest stockAddRequest = new StockAddRequest(
            List.of(stockElementDto1, stockElementDto2, stockElementDto3));

        when(stockRepository.findStocksByProductIdsAsMap(Set.of(PRODUCT1_ID, PRODUCT2_ID, PRODUCT3_ID)))
            .thenReturn(
                Stream.of(stock1, stock2, stock3).collect(Collectors.toMap(Stock::getProductId, s -> s)));
        when(stockRepository.saveAll(anyList())).thenAnswer(method -> method.getArguments()[0]);

        // when
        StockAddResponse stockAddResponse = stockService.orderStocks(stockAddRequest);

        // then

        assertTrue(stockAddResponse.success());
        assertEquals(stock1.getTotalQuantity(), PRODUCT1_STOCK_QUANTITY + PRODUCT1_QUANTITY);
        assertEquals(stock2.getTotalQuantity(), PRODUCT2_STOCK_QUANTITY + PRODUCT2_QUANTITY);
        assertEquals(stock3.getTotalQuantity(), PRODUCT3_STOCK_QUANTITY + PRODUCT3_QUANTITY);
    }
}
