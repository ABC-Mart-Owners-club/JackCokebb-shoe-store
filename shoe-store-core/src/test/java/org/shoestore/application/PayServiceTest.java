package org.shoestore.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shoestore.domain.model.order.Order;
import org.shoestore.domain.model.order.OrderRepository;
import org.shoestore.domain.model.order.OrderStatus;
import org.shoestore.domain.model.pay.CardPaySummary;
import org.shoestore.domain.model.pay.Coupon;
import org.shoestore.domain.model.pay.PayElement;
import org.shoestore.domain.model.pay.PayMethod;
import org.shoestore.domain.model.pay.PayRepository;
import org.shoestore.domain.model.pay.PayStatus;
import org.shoestore.domain.model.pay.Payment;
import org.shoestore.infra.pay.CashPayElement;
import org.shoestore.infra.pay.DaiHyunCardPayElement;
import org.shoestore.infra.pay.NaHaCardPayElement;
import org.shoestore.infra.pay.PayElementRegistry;
import org.shoestore.interfaces.pay.dto.CardPaymentSummaryRequest;
import org.shoestore.interfaces.pay.dto.CardPaymentSummaryResponse;
import org.shoestore.interfaces.pay.dto.PayElementDto;
import org.shoestore.interfaces.pay.dto.PayRequest;
import org.shoestore.interfaces.pay.dto.PayResultResponse;

@ExtendWith(MockitoExtension.class)
public class PayServiceTest {

    @Mock
    PayRepository payRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    PayService payService;

    @BeforeEach
    void setUp() {

        payService = new PayService(payRepository, orderRepository, new PayElementRegistry());
    }

    static final Long REQUESTED_AMOUNT = 1000L;
    static final Long PAY_AMOUNT_500 = 500L;
    static final Long TOTAL_AMOUNT_1000 = 1000L;

    static final Long PAY_ELEMENT_ID1 = 1L;
    static final Long PAY_ELEMENT_ID2 = 2L;

    static final Long CUSTOMER_ID1 = 1L;

    static final LocalDateTime FROM_DATE = LocalDateTime.now().minusWeeks(1);
    static final LocalDateTime TO_DATE = LocalDateTime.now();

    @Test
    @DisplayName("결제 테스트")
    public void pay() {

        // given
        Payment expected = Payment.init(REQUESTED_AMOUNT, Coupon.NONE);
        Order order = Order.init(CUSTOMER_ID1, expected.getId(), new HashMap<>());

        PayElementDto payElementDto1 = new PayElementDto(PayMethod.NAHA_CARD, PAY_AMOUNT_500);
        PayElementDto payElementDto2 = new PayElementDto(PayMethod.CASH, PAY_AMOUNT_500);
        PayElement payElement1 = new NaHaCardPayElement(PAY_ELEMENT_ID1, PAY_AMOUNT_500);
        PayElement payElement2 = new CashPayElement(PAY_ELEMENT_ID2, PAY_AMOUNT_500);

        PayRequest payRequest = new PayRequest(expected.getId(), List.of(payElementDto2, payElementDto1));

        when(payRepository.findById(expected.getId())).thenReturn(expected);
        when(payRepository.save(any(Payment.class))).thenAnswer(method -> method.getArguments()[0]);
        when(orderRepository.findByPayId(expected.getId())).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenAnswer(method -> method.getArguments()[0]);

        Payment actual = new Payment(expected.getId(), REQUESTED_AMOUNT, PayStatus.PAID, List.of(payElement1, payElement2), Coupon.NONE, expected.getCreatedAt(), expected.getPaidAt());

        // when
        PayResultResponse response = payService.pay(payRequest);


        // then
        assertEquals(actual.getPayElements().size(), expected.getPayElements().size());
        assertNotNull(expected.getPaidAt());
        assertEquals(order.getStatus(), OrderStatus.COMPLETED);
        assertTrue(expected.isAllPaid());
        assertTrue(response.success());

    }

    @Test
    @DisplayName("카드사별 결제 금액 조회 테스트")
    public void getSummary() {

        // given
        PayElement payElement1 = new DaiHyunCardPayElement(PAY_ELEMENT_ID1, PAY_AMOUNT_500);
        PayElement payElement2 = new CashPayElement(PAY_ELEMENT_ID2, PAY_AMOUNT_500);

        CardPaymentSummaryRequest request = new CardPaymentSummaryRequest(PayMethod.DAIHYUN_CARD, FROM_DATE, TO_DATE);
        List<PayElement> payElements = List.of(payElement1, payElement2);
        CardPaySummary cardPaySummary = new CardPaySummary(PayMethod.DAIHYUN_CARD, payElements);

        when(payRepository.findCardPaymentByIssuingBankAndDate(request.getPayMethod(), request.getFrom(), request.getTo())).thenReturn(cardPaySummary);

        // when
        CardPaymentSummaryResponse response = payService.getSummary(request);

        // then
        assertNotNull(response.getPayMethod());
        assertEquals(response.getTotalAmount(), TOTAL_AMOUNT_1000);
    }
}
