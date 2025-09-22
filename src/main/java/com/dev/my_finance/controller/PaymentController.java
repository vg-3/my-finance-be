package com.dev.my_finance.controller;

import com.dev.my_finance.dto.LoanDto;
import com.dev.my_finance.dto.RecordPaymentRequest;
import com.dev.my_finance.dto.SuccessResponse;
import com.dev.my_finance.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

//    loan id and amount user is paying
    @PutMapping()
    public LoanDto recordPayment(@RequestBody RecordPaymentRequest recordPaymentRequest){
       return paymentService.recordPayment(recordPaymentRequest);
    }

}
