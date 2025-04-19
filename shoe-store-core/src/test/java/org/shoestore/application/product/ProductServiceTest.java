package org.shoestore.application.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.interfaces.product.dto.ProductPriceResponse;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    Product product1 = new Product(1L, "shoe val 1", 1231L);
    Product product2 = new Product(2L, "shoe val 2", 1232L);
    Product product3 = new Product(3L, "shoe val 3", 1233L);

    ProductPriceResponse productPriceResponse1 = ProductPriceResponse.from(product1);
    ProductPriceResponse productPriceResponse2 = ProductPriceResponse.from(product2);
    ProductPriceResponse productPriceResponse3 = ProductPriceResponse.from(product3);

    @Test
    @DisplayName("Product Existence Test")
    public void testValidateProductsExist() {

        // given
        when(productRepository.findAllByIds(Set.of(1L, 2L, 3L))).thenReturn(List.of(product1, product2, product3));
        when(productRepository.findAllByIds(Set.of(1L, 2L, 4L))).thenReturn(List.of(product1, product2));
        when(productRepository.findAllByIds(Set.of(1L, 5L, 4L))).thenReturn(List.of(product1));

        // then
        assertDoesNotThrow(() -> productService.validateProductsExist((Set.of(1L, 2L, 3L))));
        assertThrows(IllegalArgumentException.class, () -> productService.validateProductsExist((Set.of(1L, 2L, 4L))));
        assertThrows(IllegalArgumentException.class, () -> productService.validateProductsExist((Set.of(1L, 5L, 4L))));
    }

    @Test
    @DisplayName("Fetch Product Price Test")
    public void testFindProductPriceById() {

        // given
        when(productRepository.findById(1L)).thenReturn(product1);
        when(productRepository.findById(2L)).thenReturn(product2);
        when(productRepository.findById(3L)).thenReturn(product3);
        when(productRepository.findById(4L)).thenReturn(null);

        // then
        assertDoesNotThrow(() -> productService.findProductPriceById(1L));
        assertThrows(IllegalArgumentException.class, () -> productService.findProductPriceById(4L));
        assertEquals(productService.findProductPriceById(1L), productPriceResponse1);
        assertEquals(productService.findProductPriceById(2L), productPriceResponse2);
        assertEquals(productService.findProductPriceById(3L), productPriceResponse3);
    }
}