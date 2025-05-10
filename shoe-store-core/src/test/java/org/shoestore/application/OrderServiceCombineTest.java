package org.shoestore.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shoestore.application.support.CustomerValidator;
import org.shoestore.application.support.ProductValidator;
import org.shoestore.domain.model.customer.CustomerRepository;
import org.shoestore.domain.model.order.Order;
import org.shoestore.domain.model.order.OrderElement;
import org.shoestore.domain.model.order.OrderRepository;
import org.shoestore.domain.model.pay.PayMethod;
import org.shoestore.domain.model.pay.PayRepository;
import org.shoestore.domain.model.pay.PayStatus;
import org.shoestore.domain.model.pay.Payment;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.infra.pay.PayElementRegistry;
import org.shoestore.interfaces.order.dto.OrderCancelRequest;
import org.shoestore.interfaces.order.dto.OrderCreateRequest;
import org.shoestore.interfaces.order.dto.OrderCreateRequest.OrderElementCreateDto;
import org.shoestore.interfaces.order.dto.OrderPartialCancelRequest;
import org.shoestore.interfaces.pay.dto.PayElementDto;

@ExtendWith(MockitoExtension.class)
public class OrderServiceCombineTest {


    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    PayRepository payRepository;
    @Mock
    CustomerRepository customerRepository;

    OrderServiceCombine orderServiceCombine;


    @BeforeEach
    public void setUp() {

        orderServiceCombine = new OrderServiceCombine(orderRepository, productRepository, payRepository, new CustomerValidator(customerRepository), new ProductValidator(productRepository), new PayElementRegistry());
    }

    private final static Long PRODUCT1_ID = 1L;
    private final static Long PRODUCT2_ID = 2L;
    private final static Long PRODUCT3_ID = 3L;

    private final static Long PRODUCT1_QUANTITY = 1L;
    private final static Long PRODUCT2_QUANTITY = 2L;
    private final static Long PRODUCT3_QUANTITY = 3L;

    private final static Long CUSTOMER1_ID = 1L;

    private final static String PRODUCT1_NAME = "Product 1";
    private final static String PRODUCT2_NAME = "Product 2";
    private final static String PRODUCT3_NAME = "Product 3";

    private final static Long PRODUCT1_PRICE = 123L;
    private final static Long PRODUCT2_PRICE = 456L;
    private final static Long PRODUCT3_PRICE = 789L;

    private final static Long ORDER1_ID = 1L;

    private final static Long REQUESTED_AMOUNT_TOTAL = 1368L;
    private final static Long REQUESTED_AMOUNT_2_3 = 1245L;


    @Test
    @DisplayName("Create Order Test")
    public void makeOrder() {

        // given
        OrderElementCreateDto orderElementCreateDto1 = new OrderElementCreateDto(PRODUCT1_ID, PRODUCT1_QUANTITY);
        OrderElementCreateDto orderElementCreateDto2 = new OrderElementCreateDto(PRODUCT2_ID, PRODUCT2_QUANTITY);
        OrderElementCreateDto orderElementCreateDto3 = new OrderElementCreateDto(PRODUCT3_ID, PRODUCT3_QUANTITY);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(CUSTOMER1_ID,
            List.of(orderElementCreateDto1, orderElementCreateDto2, orderElementCreateDto3));

        Product product1 = new Product(PRODUCT1_ID, PRODUCT1_NAME, PRODUCT1_PRICE);
        Product product2 = new Product(PRODUCT2_ID, PRODUCT2_NAME, PRODUCT2_PRICE);
        Product product3 = new Product(PRODUCT3_ID, PRODUCT3_NAME, PRODUCT3_PRICE);

        OrderElement orderElement1 = OrderElement.init(product1, PRODUCT1_QUANTITY);
        OrderElement orderElement2 = OrderElement.init(product2, PRODUCT2_QUANTITY);
        OrderElement orderElement3 = OrderElement.init(product3, PRODUCT3_QUANTITY);

        Payment payment = Payment.init(REQUESTED_AMOUNT_TOTAL);

        Order actual = Order.init(CUSTOMER1_ID, payment.getId(),
            Map.of(orderElement1.getProductId(), orderElement1, orderElement2.getProductId(),
                orderElement2, orderElement3.getProductId(), orderElement3));

        when(customerRepository.existsById(CUSTOMER1_ID)).thenReturn(true);
        when(productRepository.findAllByIds(Set.of(PRODUCT1_ID, PRODUCT2_ID, PRODUCT3_ID))).thenReturn(List.of(product1, product2, product3));
        when(orderRepository.save(any(Order.class))).thenReturn(actual);
        when(payRepository.save(any(Payment.class))).thenAnswer(method -> method.getArguments()[0]);

        // when
        Order test = orderServiceCombine.makeOrder(orderCreateRequest);

        // then
        assertEquals(actual, test);
    }

    @Test
    @DisplayName("Cancellation Order Test")
    public void testCancelOrder() {

        // given
        OrderCancelRequest request = new OrderCancelRequest(ORDER1_ID);

        OrderElement orderElement1 = new OrderElement(PRODUCT1_ID, PRODUCT1_PRICE, PRODUCT1_QUANTITY, false);
        OrderElement orderElement2 = new OrderElement(PRODUCT2_ID, PRODUCT2_PRICE, PRODUCT2_QUANTITY, false);
        OrderElement orderElement3 = new OrderElement(PRODUCT3_ID, PRODUCT3_PRICE, PRODUCT3_QUANTITY, false);

        Map<Long, OrderElement> elements = Map.of(orderElement1.getProductId(), orderElement1,
            orderElement2.getProductId(), orderElement2, orderElement3.getProductId(),
            orderElement3);

        Payment payment = Payment.init(REQUESTED_AMOUNT_TOTAL);

        Order actual = new Order(ORDER1_ID, CUSTOMER1_ID, payment.getId(), elements);


        OrderElement orderElement1After = new OrderElement(PRODUCT1_ID, PRODUCT1_PRICE, PRODUCT1_QUANTITY, true);
        OrderElement orderElement2After = new OrderElement(PRODUCT2_ID, PRODUCT2_PRICE, PRODUCT2_QUANTITY, true);
        OrderElement orderElement3After = new OrderElement(PRODUCT3_ID, PRODUCT3_PRICE, PRODUCT3_QUANTITY, true);

        Map<Long, OrderElement> elementsAfter = Map.of(orderElement1After.getProductId(), orderElement1After,
            orderElement2After.getProductId(), orderElement2After, orderElement3After.getProductId(),
            orderElement3After);

        Order actualOrderAfter = new Order(ORDER1_ID, CUSTOMER1_ID, payment.getId(), elementsAfter);

        // when
        when(orderRepository.findById(request.getOrderId())).thenReturn(actual);
        when(orderRepository.save(any(Order.class))).thenAnswer(method -> method.getArguments()[0]);

        // then
        Order expected = orderServiceCombine.cancelOrder(request);
        assertEquals(expected, actualOrderAfter);
    }

    @Test
    @DisplayName("Partial Cancellation Order Test")
    public void partialCancelOrderPartially() {

        // given

        PayElementDto payElementDto2 = new PayElementDto(PayMethod.NAHA_CARD, PRODUCT2_PRICE * PRODUCT2_QUANTITY);
        PayElementDto payElementDto3 = new PayElementDto(PayMethod.CASH, PRODUCT3_PRICE * PRODUCT3_QUANTITY);

        OrderPartialCancelRequest request = new OrderPartialCancelRequest(ORDER1_ID, List.of(PRODUCT1_ID), List.of(payElementDto2, payElementDto3));

        OrderElement orderElement1 = new OrderElement(PRODUCT1_ID, PRODUCT1_PRICE, PRODUCT1_QUANTITY, false);
        OrderElement orderElement2 = new OrderElement(PRODUCT2_ID, PRODUCT2_PRICE, PRODUCT2_QUANTITY, false);
        OrderElement orderElement3 = new OrderElement(PRODUCT3_ID, PRODUCT3_PRICE, PRODUCT3_QUANTITY, false);

        Map<Long, OrderElement> elements = Map.of(orderElement1.getProductId(), orderElement1,
            orderElement2.getProductId(), orderElement2, orderElement3.getProductId(),
            orderElement3);

        Payment payment = Payment.init(REQUESTED_AMOUNT_TOTAL);
        Payment newPayment = Payment.init(REQUESTED_AMOUNT_2_3);

        Order actual = new Order(ORDER1_ID, CUSTOMER1_ID, payment.getId(), elements);


        OrderElement orderElement1After = new OrderElement(PRODUCT1_ID, PRODUCT1_PRICE, PRODUCT1_QUANTITY, true);
        OrderElement orderElement2After = new OrderElement(PRODUCT2_ID, PRODUCT2_PRICE, PRODUCT2_QUANTITY, false);
        OrderElement orderElement3After = new OrderElement(PRODUCT3_ID, PRODUCT3_PRICE, PRODUCT3_QUANTITY, false);

        Map<Long, OrderElement> elementsAfter = Map.of(orderElement1After.getProductId(), orderElement1After,
            orderElement2After.getProductId(), orderElement2After, orderElement3After.getProductId(),
            orderElement3After);

        Order actualOrderAfter = new Order(ORDER1_ID, CUSTOMER1_ID, newPayment.getId(), elementsAfter);

        // when
        when(orderRepository.findById(request.getOrderId())).thenReturn(actual);
        when(orderRepository.save(any(Order.class))).thenAnswer(method -> method.getArguments()[0]);
        when(payRepository.findById(actual.getPayId())).thenReturn(payment);

        // then
        assertEquals(orderServiceCombine.cancelOrderPartially(request), actualOrderAfter);
        assertEquals(payment.getPayStatus(), PayStatus.CANCELED);
    }
}
