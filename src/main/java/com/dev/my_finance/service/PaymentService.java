package com.dev.my_finance.service;

import com.dev.my_finance.dto.*;
import com.dev.my_finance.entity.Loan;
import com.dev.my_finance.entity.Payment;
import com.dev.my_finance.enumeration.LoanStatus;
import com.dev.my_finance.enumeration.PaymentType;
import com.dev.my_finance.exceptions.ResourceNotFoundException;
import com.dev.my_finance.repository.LoanRepository;
import com.dev.my_finance.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanRepository loanRepository;

    public LoanDto recordPayment(RecordPaymentRequest recordPaymentRequest){


        if(recordPaymentRequest.loanId() == null || recordPaymentRequest.paymentType() == null
                || recordPaymentRequest.amount() == null ) {
            throw new IllegalArgumentException("Required arguments loanId, paymentType, amount");
        }

        Loan loan = loanRepository.findById(recordPaymentRequest.loanId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if(loan.getStatus().equals(LoanStatus.CLOSED)){
            throw new UnsupportedOperationException("Loan is already closed. Updates are not allowed.");
        }

        List<Payment> payments = loan.getPayments();

        Payment payment =  Payment.builder()
                .amountPaid(recordPaymentRequest.amount())
                .paymentDate(LocalDateTime.now())
                .paymentType(recordPaymentRequest.paymentType())
                .loan(loan)
                .build();

        if(recordPaymentRequest.paymentType() == PaymentType.INTEREST){
            payments.add(payment);
        }

        if(recordPaymentRequest.paymentType() == PaymentType.PRINCIPAL){
            if(loan.getRemainingAmount().equals(recordPaymentRequest.amount())){
                loan.setStatus(LoanStatus.CLOSED);
            }
            loan.setRemainingAmount(loan.getRemainingAmount() - recordPaymentRequest.amount());
            payments.add(payment);
        }

        if(recordPaymentRequest.paymentType() == PaymentType.EMI){
            if(getTotalAmountPaid(payments, recordPaymentRequest.amount()).equals(loan.getPrincipalAmount())){
                loan.setStatus(LoanStatus.CLOSED);
            }
            loan.setRemainingAmount(loan.getRemainingAmount() - recordPaymentRequest.amount());
            payments.add(payment);
        }

        var savedLoan =loanRepository.save(loan);

        LenderDto lenderDto = LenderDto.builder()
                .id(loan.getLender().getId())
                .name(loan.getLender().getFirstName() + loan.getLender().getLastName())
                .build();

        ReceiverDto receiverdto = ReceiverDto.builder()
                .id(loan.getReceiver().getId())
                .name(loan.getReceiver().getName())
                .contact(loan.getReceiver().getContact())
                .build();


        return  LoanDto.builder()
                .loanId(savedLoan.getId())
                .principalAmount(savedLoan.getPrincipalAmount())
                .startDate(savedLoan.getStartDate())
                .status(savedLoan.getStatus())
                .loanType(savedLoan.getLoanType())
                .interestRate(savedLoan.getInterestRate())
                .remainingAmount(savedLoan.getRemainingAmount())
                .receiver(receiverdto)
                .lender(lenderDto)
                .payments(savedLoan.getPayments())
                .build();
    }

    private Double getTotalAmountPaid(List<Payment> payments, Double currentPayment) {
        double totalAmount = payments.stream()
                .mapToDouble(Payment::getAmountPaid)
                .sum();
        return totalAmount + currentPayment;
    }

}
