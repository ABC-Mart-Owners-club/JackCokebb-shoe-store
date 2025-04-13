package org.shoestore.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.shoestore.interfaces.order.dto.OrderCancelRequest;
import org.shoestore.interfaces.order.dto.OrderCreateRequestDto;
import org.shoestore.interfaces.order.dto.OrderPartialCancelRequest;
import org.shoestore.domain.model.order.Order;
import org.shoestore.domain.model.order.OrderRepository;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductPriceInfo;
import org.shoestore.domain.model.product.ProductRepository;
import org.shoestore.application.customer.CustomerService;
import org.shoestore.application.product.ProductService;

public class OrderServiceCombine {

    private final CustomerService customerService;
    private final ProductService productService;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceCombine(CustomerService customerService, ProductService productService, OrderRepository orderRepository, ProductRepository productRepository) {
        this.customerService = customerService;
        this.productService = productService;

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order makeOrder(OrderCreateRequestDto requestDto) {

        Order order = requestDto.toOrderDomain();

        validateNewOrder(order);

        Map<Long, ProductPriceInfo> priceInfoMap = getPriceInfoMap(order);
        order.calculateTotalPrice(priceInfoMap);

        return orderRepository.save(order);
    }

    public Order cancelOrder(OrderCancelRequest requestDto) {

        Order order = orderRepository.findById(requestDto.getOrderId());
        order.cancelAll();

        return orderRepository.save(order);
    }

    public Order cancelOrderPartially(OrderPartialCancelRequest requestDto) {

        Order order = orderRepository.findById(requestDto.getOrderId());
        order.cancel(requestDto.getOrderElementIds());

        return orderRepository.save(order);
    }

    private Map<Long, ProductPriceInfo> getPriceInfoMap(Order order) {
        List<Product> products = productRepository.findAllByIds(order.getProductIds());

        return products.stream()
            .map(ProductPriceInfo::from)
            .collect(Collectors.toMap(ProductPriceInfo::getProductId, info -> info));
    }

    private void validateNewOrder(Order order) {

        customerService.validateCustomerExist(order.getCustomerId());
        productService.validateProductsExist(order.getProductIds());
    }
}
