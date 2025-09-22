package com.dev.my_finance.dto;

import java.time.LocalDateTime;

public record PaymentsDto(
        Long id,
        Double amountPaid,
        LocalDateTime paymentDate
        ) {}
