package org.shoestore.application.support;

import java.util.List;
import java.util.Set;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;

public class ProductValidator {

    private final ProductRepository productRepository;

    public ProductValidator(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    public void validateProductsExist(Set<Long> productIds) {

        List<Product> products = productRepository.findAllByIds(productIds);
        if (products.size() != productIds.size()) {

            throw new IllegalArgumentException("Invalid product included");
        }
    }
}
