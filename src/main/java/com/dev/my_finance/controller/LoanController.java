package com.dev.my_finance.controller;

import com.dev.my_finance.dto.*;
import com.dev.my_finance.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/lender/{lenderId}")
    public List<LoanDto> getLoansByLenderId(@PathVariable Long lenderId){
        return loanService.getLoansByLenderId(lenderId);
    }

    @GetMapping("/{loanId}")
    public LoanDto getLoanByLoanId(@PathVariable Long loanId){
        return loanService.getLoanByLoanId(loanId);
    }

    @PostMapping("")
    public LoanDto createLoan(@RequestBody LoanCreateRequest loan){
        return loanService.createLoan(loan);
    }

    @PutMapping("")
    public SuccessResponse updateLoan(@RequestBody LoanUpdateRequest loanUpdateRequest){
        return loanService.updateLoan(loanUpdateRequest);
    }

    @DeleteMapping("/{loanId}")
    public SuccessResponse deleteLoan(@PathVariable Long loanId){
        return loanService.deleteLoan(loanId);
    }

    @PutMapping("/{loanId}")
    public SuccessResponse closeLoan(@PathVariable Long loanId){
        return loanService.closeLoan(loanId);
    }

}
