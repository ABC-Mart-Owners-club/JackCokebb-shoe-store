package org.shoestore.application;

import java.util.List;
import org.shoestore.domain.model.pay.Payment;
import org.shoestore.domain.model.pay.PayRepository;
import org.shoestore.domain.model.pay.Payment.PayElement;
import org.shoestore.interfaces.pay.dto.CardPaymentSummaryRequest;
import org.shoestore.interfaces.pay.dto.CardPaymentSummaryResponse;
import org.shoestore.interfaces.pay.dto.PayRequest;
import org.shoestore.interfaces.pay.dto.PayResultResponse;

public class PayService {

    private final PayRepository payRepository;

    public PayService(PayRepository payRepository) {
        this.payRepository = payRepository;
    }

    public PayResultResponse pay(PayRequest requestDto) {

        Payment payment = payRepository.findById(requestDto.getPayId());
        List<PayElement> payElements = requestDto.getPayElements().stream()
            .map(req -> PayElement.init(req.getPayMethod(), req.getIssuingBank(),
                req.getPayAmount()))
            .toList();

        payment.pay(payElements);

        return new PayResultResponse(payRepository.save(payment) != null);
    }

    public CardPaymentSummaryResponse getSummary(CardPaymentSummaryRequest request) {

        List<PayElement> cardPayments = payRepository.findCardPaymentByIssuingBankAndDate(
            request.getIssuingBank(), request.getFrom(), request.getTo());

        long sumAmount = cardPayments.stream().mapToLong(PayElement::getPayAmount).sum();

        return new CardPaymentSummaryResponse(request.getIssuingBank(), sumAmount);
    }
}
