package com.dev.my_finance.dto;

import com.dev.my_finance.enumeration.LoanStatus;

public record LoanUpdateRequest(Long loanId, LoanStatus status) {
}
