package com.dev.my_finance.dto;

import com.dev.my_finance.entity.Receiver;
import com.dev.my_finance.enumeration.LoanStatus;
import com.dev.my_finance.enumeration.LoanType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoanCreateRequest(Double principalAmount,
                                float interestRate,
                                LoanType loanType,
                                LoanStatus status,
                                LocalDateTime startDate,
                                LocalDateTime endDate,
                                Long userId,
                                Long receiverId
                                ) {}