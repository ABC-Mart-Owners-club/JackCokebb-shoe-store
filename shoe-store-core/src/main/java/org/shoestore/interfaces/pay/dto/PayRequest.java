package org.shoestore.interfaces.pay.dto;

import java.util.List;

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
}
