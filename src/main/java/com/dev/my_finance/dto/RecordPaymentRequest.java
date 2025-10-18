package com.dev.my_finance.dto;

import com.dev.my_finance.enumeration.PaymentType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecordPaymentRequest(Long loanId,
                                   Double amount,
                                   PaymentType paymentType,
                                   LocalDateTime paymentDate

) {
}
