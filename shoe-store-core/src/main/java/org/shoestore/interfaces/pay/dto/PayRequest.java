package org.shoestore.interfaces.pay.dto;

import java.util.List;
import org.shoestore.domain.model.pay.IssuingBank;
import org.shoestore.domain.model.pay.PayMethod;

public class PayRequest {

    private Long payId;

    private List<PayElementDto> payElements;

    public PayRequest(Long payId, List<PayElementDto> payElements) {
        this.payId = payId;
        this.payElements = payElements;
    }

    public Long getPayId() {

        return payId;
    }

    public List<PayElementDto> getPayElements() {

        return payElements;
    }

    public static class PayElementDto {

        private PayMethod payMethod;

        private IssuingBank issuingBank;

        private Long payAmount;

        public PayElementDto(PayMethod payMethod, IssuingBank issuingBank, Long payAmount) {
            this.payMethod = payMethod;
            this.issuingBank = issuingBank;
            this.payAmount = payAmount;
        }

        public PayMethod getPayMethod() {

            return payMethod;
        }

        public IssuingBank getIssuingBank() {

            return issuingBank;
        }

        public Long getPayAmount() {

            return payAmount;
        }
    }
}
