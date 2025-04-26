package org.shoestore.domain.model.pay;

import java.time.LocalDateTime;
import java.util.List;
import org.shoestore.domain.model.pay.Payment.IssuingBank;
import org.shoestore.domain.model.pay.Payment.PayElement;

public interface PayRepository {

    Payment save(Payment payment);

    Payment findById(Long id);

    CardPaySummary findCardPaymentByIssuingBankAndDate(IssuingBank issuingBank, LocalDateTime from, LocalDateTime to);
}
