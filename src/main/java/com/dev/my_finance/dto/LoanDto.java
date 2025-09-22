package com.dev.my_finance.dto;

import com.dev.my_finance.entity.Payment;
import com.dev.my_finance.enumeration.LoanStatus;
import com.dev.my_finance.enumeration.LoanType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoanDto(Long loanId,
                      Double principalAmount,
                      float interestRate,
                      LoanType loanType,
                      LoanStatus status,
                      LocalDateTime startDate,
                      LocalDateTime endDate,
                      LenderDto lender,
                      ReceiverDto receiver,
                      List<Payment> payments,
                      Double remainingAmount
){}
