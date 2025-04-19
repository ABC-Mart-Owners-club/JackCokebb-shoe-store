package org.shoestore.domain.model.product;

public class ProductPriceInfo {

    private Long productId;

    private Long price;

    public Long getProductId() {

        return productId;
    }

    public Long getPrice() {

        return price;
    }

    public ProductPriceInfo(Long productId, Long price) {
        this.productId = productId;
        this.price = price;
    }

    public static ProductPriceInfo from(Product product) {

        return new ProductPriceInfo(product.getId(), product.getPrice());
    }
}
