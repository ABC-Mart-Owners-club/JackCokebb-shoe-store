package org.shoestore.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.shoestore.application.support.CustomerValidator;
import org.shoestore.application.support.ProductValidator;
import org.shoestore.domain.model.order.OrderElement;
import org.shoestore.domain.model.pay.PayElement;
import org.shoestore.domain.model.pay.Payment;
import org.shoestore.domain.model.pay.PayRepository;
import org.shoestore.infra.pay.PayElementRegistry;
import org.shoestore.interfaces.order.dto.OrderCancelRequest;
import org.shoestore.interfaces.order.dto.OrderCreateRequest;
import org.shoestore.interfaces.order.dto.OrderPartialCancelRequest;
import org.shoestore.domain.model.order.Order;
import org.shoestore.domain.model.order.OrderRepository;
import org.shoestore.domain.model.product.Product;
import org.shoestore.domain.model.product.ProductRepository;

public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PayRepository payRepository;
    private final CustomerValidator customerValidator;
    private final ProductValidator productValidator;
    private final PayElementRegistry payElementRegistry;


    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, PayRepository payRepository,
        CustomerValidator customerValidator, ProductValidator productValidator,
        PayElementRegistry payElementRegistry) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.payRepository = payRepository;
        this.customerValidator = customerValidator;
        this.productValidator = productValidator;
        this.payElementRegistry = payElementRegistry;
    }

    public Order makeOrder(OrderCreateRequest requestDto) {

        customerValidator.validateCustomerExist(requestDto.getCustomerId());
        productValidator.validateProductsExist(requestDto.getOrderedProductIds());

        Map<Long, Product> productMap = productRepository.findAllByIds(
                requestDto.getOrderedProductIds())
            .stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        Map<Long, OrderElement> elements = requestDto.getOrderElements().stream()
            .map(e -> OrderElement.init(productMap.get(e.getProductId()), e.getQuantity()))
            .collect(Collectors.toMap(OrderElement::getProductId, Function.identity()));

        // 재고 차감
        elements.forEach((productId, element) -> productMap.get(productId).minusStockIfEnoughOrElseThrow(element.getQuantity()));

        // 결제 정보 생성 (실제 결제 x)
        Payment payment = Payment.init(elements.values().stream().toList());

        Order order = Order.init(requestDto.getCustomerId(), payment.getId(), elements);

        productRepository.saveAll(productMap.values().stream().toList());
        payRepository.save(payment);
        return orderRepository.save(order);
    }

    public Order cancelOrder(OrderCancelRequest requestDto) {

        Order order = orderRepository.findById(requestDto.getOrderId());
        order.cancelAll();

        Payment payment = payRepository.findById(order.getPayId());
        payment.cancel();

        // 재고 재입고 처리
        Map<Long, Product> productMap = productRepository.findAllByIdsAsMap(order.getProductIds());
        order.getOrderElements().forEach((element) -> productMap.get(element.getProductId()).addStock(element.getQuantity()));

        productRepository.saveAll(productMap.values().stream().toList());
        payRepository.save(payment);
        return orderRepository.save(order);
    }

    public Order cancelOrderPartially(OrderPartialCancelRequest requestDto) {

        Order order = orderRepository.findById(requestDto.getOrderId());
        order.cancel(requestDto.getProductIds());

        Payment previousPayment = payRepository.findById(order.getPayId());
        previousPayment.cancel();

        // 결제 재진행
        Payment newPayment = Payment.init(order.getTotalPrice());
        List<PayElement> payElements = requestDto.getPayElements().stream()
            .map(req -> payElementRegistry.get(req.getPayMethod()).apply(req.getPayAmount()))
            .toList();
        newPayment.pay(payElements);
        order.updatePayment(newPayment.getId());

        // 재고 재입고 처리
        Map<Long, Product> productMap = productRepository.findAllByIdsAsMap(requestDto.getProductIdsAsSet());
        order.getOrderElements().stream()
            .filter(element -> requestDto.getProductIdsAsSet().contains(element.getProductId()))
            .forEach(element -> productMap.get(element.getProductId()).addStock(element.getQuantity()));

        productRepository.saveAll(productMap.values().stream().toList());
        payRepository.saveAll(List.of(newPayment, previousPayment));
        return orderRepository.save(order);
    }
}
