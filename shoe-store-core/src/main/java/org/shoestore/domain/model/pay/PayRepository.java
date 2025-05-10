package org.shoestore.domain.model.pay;

import java.time.LocalDateTime;
import java.util.List;

public interface PayRepository {

    Payment save(Payment payment);

    Payment findById(Long id);

    CardPaySummary findCardPaymentByIssuingBankAndDate(PayMethod payMethod, LocalDateTime from, LocalDateTime to);
}
