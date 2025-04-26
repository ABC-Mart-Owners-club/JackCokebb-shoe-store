package org.shoestore.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shoestore.domain.model.pay.PayRepository;
import org.shoestore.domain.model.pay.Payment;
import org.shoestore.domain.model.pay.Payment.IssuingBank;
import org.shoestore.domain.model.pay.Payment.PayElement;
import org.shoestore.domain.model.pay.Payment.PayMethod;
import org.shoestore.interfaces.pay.dto.CardPaymentSummaryRequest;
import org.shoestore.interfaces.pay.dto.CardPaymentSummaryResponse;
import org.shoestore.interfaces.pay.dto.PayRequest;
import org.shoestore.interfaces.pay.dto.PayRequest.PayElementDto;
import org.shoestore.interfaces.pay.dto.PayResultResponse;

@ExtendWith(MockitoExtension.class)
public class PayServiceTest {

    @Mock
    PayRepository payRepository;

    @InjectMocks
    PayService payService;

    static final Long REQUESTED_AMOUNT = 1000L;
    static final Long PAY_AMOUNT_500 = 500L;
    static final Long TOTAL_AMOUNT_1000 = 1000L;

    static final Long PAY_ELEMENT_ID1 = 1L;
    static final Long PAY_ELEMENT_ID2 = 2L;

    static final LocalDateTime FROM_DATE = LocalDateTime.now().minusWeeks(1);
    static final LocalDateTime TO_DATE = LocalDateTime.now();

    @Test
    @DisplayName("결제 테스트")
    public void pay() {

        // given
        Payment expected = Payment.init(REQUESTED_AMOUNT);

        PayElementDto payElementDto1 = new PayElementDto(PayMethod.CARD, IssuingBank.DAIHYUN, PAY_AMOUNT_500);
        PayElementDto payElementDto2 = new PayElementDto(PayMethod.CASH, null, PAY_AMOUNT_500);
        PayElement payElement1 = new PayElement(PAY_ELEMENT_ID1, PayMethod.CARD, IssuingBank.DAIHYUN, PAY_AMOUNT_500);
        PayElement payElement2 = new PayElement(PAY_ELEMENT_ID2, PayMethod.CASH, null, PAY_AMOUNT_500);

        PayRequest payRequest = new PayRequest(expected.getId(), List.of(payElementDto2, payElementDto1));

        when(payRepository.findById(expected.getId())).thenReturn(expected);
        when(payRepository.save(any(Payment.class))).thenAnswer(method -> method.getArguments()[0]);

        Payment actual = new Payment(expected.getId(), REQUESTED_AMOUNT, List.of(payElement1, payElement2), expected.getCreatedAt(), expected.getPaidAt());

        // when
        PayResultResponse response = payService.pay(payRequest);


        // then
        assertEquals(actual.getPayElements().size(), expected.getPayElements().size());
        assertNotNull(expected.getPaidAt());
        assertTrue(response.success());

    }

    @Test
    @DisplayName("카드사별 결제 금액 조회 테스트")
    public void getSummary() {

        // given
        PayElement payElement1 = new PayElement(PAY_ELEMENT_ID1, PayMethod.CARD, IssuingBank.DAIHYUN, PAY_AMOUNT_500);
        PayElement payElement2 = new PayElement(PAY_ELEMENT_ID2, PayMethod.CASH, null, PAY_AMOUNT_500);

        CardPaymentSummaryRequest request = new CardPaymentSummaryRequest(IssuingBank.DAIHYUN, FROM_DATE, TO_DATE);
        List<PayElement> payElements = List.of(payElement1, payElement2);

        when(payRepository.findCardPaymentByIssuingBankAndDate(request.getIssuingBank(), request.getFrom(), request.getTo())).thenReturn(payElements);

        // when
        CardPaymentSummaryResponse response = payService.getSummary(request);

        // then
        assertNotNull(response.getIssuingBank());
        assertEquals(response.getTotalAmount(), TOTAL_AMOUNT_1000);
    }
}
