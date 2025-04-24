package org.shoestore.application;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.shoestore.domain.model.order.OrderElement;
import org.shoestore.interfaces.order.dto.OrderCancelRequest;
import org.shoestore.interfaces.order.dto.OrderCreateRequestDto;
import org.shoestore.interfaces.order.dto.OrderPartialCancelRequest;
import org.shoestore.domain.model.order.Order;
import org.shoestore.domain.model.order.OrderRepository;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.application.customer.CustomerService;
import org.shoestore.application.product.ProductService;

public class OrderServiceCombine {

    private final CustomerService customerService;
    private final ProductService productService;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceCombine(CustomerService customerService, ProductService productService,
        OrderRepository orderRepository, ProductRepository productRepository) {
        this.customerService = customerService;
        this.productService = productService;

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order makeOrder(OrderCreateRequestDto requestDto) {

        Map<Long, Product> productMap = productRepository.findAllByIds(
                requestDto.getOrderedProductIds())
            .stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        Map<Long, OrderElement> elements = requestDto.getOrderElements().stream()
            .map(e -> OrderElement.init(productMap.get(e.getProductId()), e.getQuantity()))
            .collect(Collectors.toMap(OrderElement::getProductId, Function.identity()));

        Order order = Order.init(requestDto.getCustomerId(), elements);
        validateNewOrder(order);

        return orderRepository.save(order);
    }

    public Order cancelOrder(OrderCancelRequest requestDto) {

        Order order = orderRepository.findById(requestDto.getOrderId());
        order.cancelAll();

        return orderRepository.save(order);
    }

    public Order cancelOrderPartially(OrderPartialCancelRequest requestDto) {

        Order order = orderRepository.findById(requestDto.getOrderId());
        order.cancel(requestDto.getProductIds());

        return orderRepository.save(order);
    }

    private void validateNewOrder(Order order) {

        customerService.validateCustomerExist(order.getCustomerId());
        productService.validateProductsExist(order.getProductIds());
    }
}
