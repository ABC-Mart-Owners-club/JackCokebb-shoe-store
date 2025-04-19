package org.shoestore.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shoestore.application.customer.CustomerService;
import org.shoestore.application.product.ProductService;
import org.shoestore.domain.model.order.Order;
import org.shoestore.domain.model.order.OrderRepository;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.interfaces.order.dto.OrderCreateRequestDto;
import org.shoestore.interfaces.order.dto.OrderCreateRequestDto.OrderElementCreateDto;

@ExtendWith(MockitoExtension.class)
public class OrderServiceCombineTest {


    @Mock
    CustomerService customerService;
    @Mock
    ProductService productService;
    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    OrderServiceCombine orderServiceCombine;


    @Test
    @DisplayName("Create Order Test")
    public void makeOrder() {

        // given
        OrderElementCreateDto orderElementCreateDto1 = new OrderElementCreateDto(1L, 1L);
        OrderElementCreateDto orderElementCreateDto2 = new OrderElementCreateDto(2L, 2L);
        OrderElementCreateDto orderElementCreateDto3 = new OrderElementCreateDto(3L, 3L);
        OrderCreateRequestDto orderCreateRequestDto = new OrderCreateRequestDto(1L,
            List.of(orderElementCreateDto1, orderElementCreateDto2, orderElementCreateDto3));

        Product product1 = new Product(1L, "shoe val 1", 1231L);
        Product product2 = new Product(2L, "shoe val 2", 1232L);
        Product product3 = new Product(3L, "shoe val 3", 1233L);

        // when
        doNothing().when(customerService).validateCustomerExist(1L);
        doNothing().when(productService).validateProductsExist(Set.of(1L, 2L, 3L));
        when(productRepository.findAllByIds(Set.of(1L, 2L, 3L))).thenReturn(List.of(product1, product2, product3));

        // then
        Order test = orderServiceCombine.makeOrder(orderCreateRequestDto); // TODO: 왜 null이 나오지~?~?
        Order actual = orderCreateRequestDto.toOrderDomain();
        assertEquals(actual, test);
    }
}
