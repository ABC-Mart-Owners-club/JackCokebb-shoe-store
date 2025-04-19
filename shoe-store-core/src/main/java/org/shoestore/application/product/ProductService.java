package org.shoestore.application.product;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.interfaces.product.dto.ProductPriceResponse;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    public void validateProductsExist(Set<Long> productIds) {

        List<Product> products = productRepository.findAllByIds(productIds);
        if (products.size() != productIds.size()) {

            throw new IllegalArgumentException("Invalid product included");
        }
    }

    public ProductPriceResponse findProductPriceById(Long id) {

        Product product = Optional.ofNullable(productRepository.findById(id))
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        return ProductPriceResponse.from(product);
    }
}
