package org.shoestore.application;

import java.util.List;
import org.shoestore.domain.model.order.Order;
import org.shoestore.domain.model.order.OrderRepository;
import org.shoestore.domain.model.pay.CardPaySummary;
import org.shoestore.domain.model.pay.PayElement;
import org.shoestore.domain.model.pay.Payment;
import org.shoestore.domain.model.pay.PayRepository;
import org.shoestore.interfaces.pay.dto.CardPaymentSummaryRequest;
import org.shoestore.interfaces.pay.dto.CardPaymentSummaryResponse;
import org.shoestore.interfaces.pay.dto.PayRequest;
import org.shoestore.interfaces.pay.dto.PayResultResponse;

public class PayService {

    private final PayRepository payRepository;
    private final OrderRepository orderRepository;

    public PayService(PayRepository payRepository, OrderRepository orderRepository) {
        this.payRepository = payRepository;
        this.orderRepository = orderRepository;
    }

    public PayResultResponse pay(PayRequest requestDto) {

        Payment payment = payRepository.findById(requestDto.getPayId());
        List<PayElement> payElements = requestDto.getPayElements().stream()
            .map(req -> PayElement.init(req.getPayMethod(), req.getIssuingBank(),
                req.getPayAmount()))
            .toList();

        payment.pay(payElements);

        Order order = orderRepository.findByPayId(requestDto.getPayId());
        order.updateOrderStatusByPaidStatus(payment.isAllPaid());

        return new PayResultResponse(payRepository.save(payment) != null && orderRepository.save(order) != null);
    }

    public CardPaymentSummaryResponse getSummary(CardPaymentSummaryRequest request) {

        CardPaySummary summary = payRepository.findCardPaymentByIssuingBankAndDate(
            request.getIssuingBank(), request.getFrom(), request.getTo());

        return new CardPaymentSummaryResponse(summary.getIssuingBank(), summary.getTotalAmount());
    }
}
