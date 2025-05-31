package org.shoestore.interfaces.product.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StockAddRequest {

    private List<StockElementDto> stockElementDtos;

    public StockAddRequest(List<StockElementDto> stockElementDtos) {

        this.stockElementDtos = stockElementDtos;
    }

    public static class StockElementDto {

        private Long productId;

        private Long quantity;

        public StockElementDto(Long productId, Long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {

            return productId;
        }

        public Long getQuantity() {

            return quantity;
        }
    }


    public List<StockElementDto> getStockElements() {

        return stockElementDtos;
    }

    public Set<Long> getProductIdsAsSet() {

        return stockElementDtos.stream().map(stockElementDto -> stockElementDto.productId).collect(Collectors.toSet());
    }
}
