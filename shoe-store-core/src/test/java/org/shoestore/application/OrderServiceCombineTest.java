package org.shoestore.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
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
import org.shoestore.domain.model.order.OrderElement;
import org.shoestore.domain.model.order.OrderRepository;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.interfaces.order.dto.OrderCancelRequest;
import org.shoestore.interfaces.order.dto.OrderCreateRequestDto;
import org.shoestore.interfaces.order.dto.OrderCreateRequestDto.OrderElementCreateDto;
import org.shoestore.interfaces.order.dto.OrderPartialCancelRequest;

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

        Order actual = orderCreateRequestDto.toOrderDomain();

        // when
        doNothing().when(customerService).validateCustomerExist(1L);
        doNothing().when(productService).validateProductsExist(Set.of(1L, 2L, 3L));
        when(productRepository.findAllByIds(Set.of(1L, 2L, 3L))).thenReturn(List.of(product1, product2, product3));
        when(orderRepository.save(any(Order.class))).thenReturn(actual);

        // then
        Order test = orderServiceCombine.makeOrder(orderCreateRequestDto);
        assertEquals(actual, test);
    }

    @Test
    @DisplayName("Cancellation Order Test")
    public void testCancelOrder() {

        // given
        OrderCancelRequest request = new OrderCancelRequest(1L);

        OrderElement orderElement1 = new OrderElement(1L, 1L, 1L, 1L, false);
        OrderElement orderElement2 = new OrderElement(2L, 1L, 2L, 1L, false);
        OrderElement orderElement3 = new OrderElement(3L, 1L, 3L, 1L, false);

        Map<Long, OrderElement> elements = Map.of(orderElement1.getProductId(), orderElement1,
            orderElement2.getProductId(), orderElement2, orderElement3.getProductId(),
            orderElement3);

        Order actualOrder = new Order(1L, 1L, elements, 1L);

        OrderElement orderElement1After = new OrderElement(1L, 1L, 1L, 1L, true);
        OrderElement orderElement2After = new OrderElement(2L, 1L, 2L, 1L, true);
        OrderElement orderElement3After = new OrderElement(3L, 1L, 3L, 1L, true);

        Map<Long, OrderElement> elementsAfter = Map.of(orderElement1.getProductId(), orderElement1,
            orderElement2.getProductId(), orderElement2, orderElement3.getProductId(),
            orderElement3);

        Order actualOrderAfter = new Order(1L, 1L, elementsAfter, 1L);

        // when
        when(orderRepository.findById(request.getOrderId())).thenReturn(actualOrder);
        when(orderRepository.save(any(Order.class))).thenAnswer(method -> method.getArguments()[0]);

        // then

        assertEquals(orderServiceCombine.cancelOrder(request), actualOrderAfter);
    }

    public Order cancelOrderPartially(OrderPartialCancelRequest requestDto) {

        Order order = orderRepository.findById(requestDto.getOrderId());
        order.cancel(requestDto.getOrderElementIds());

        return orderRepository.save(order);
    }
}
