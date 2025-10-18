package com.dev.my_finance.service;

import com.dev.my_finance.dto.*;
import com.dev.my_finance.entity.Loan;
import com.dev.my_finance.entity.Receiver;
import com.dev.my_finance.entity.User;
import com.dev.my_finance.enumeration.LoanStatus;
import com.dev.my_finance.enumeration.LoanType;
import com.dev.my_finance.exceptions.ResourceNotFoundException;
import com.dev.my_finance.lib.Utils;
import com.dev.my_finance.repository.LoanRepository;
import com.dev.my_finance.repository.ReceiverRepository;
import com.dev.my_finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final ReceiverRepository receiverRepository;
    private final UserRepository userRepository;

    public List<LoanDto> getLoansByLenderId(Long lenderId){
        List<Loan> loans = loanRepository.findByLenderId(lenderId);

        List<LoanDto> response = new ArrayList<>();
        for(Loan loan : loans){
            LenderDto lender = LenderDto.builder()
                    .id(loan.getLender().getId())
                    .name(loan.getLender().getFirstName() + loan.getLender().getLastName())
                    .build();
            ReceiverDto receiver = ReceiverDto.builder()
                    .id(loan.getReceiver().getId())
                    .name(loan.getReceiver().getName())
                    .contact(loan.getReceiver().getContact())
                    .build();

            response.add(LoanDto.builder()
                    .loanId(loan.getId())
                    .principalAmount(loan.getPrincipalAmount())
                    .interestRate(loan.getInterestRate())
                    .loanType(loan.getLoanType())
                    .status(loan.getStatus())
                    .startDate(loan.getStartDate())
                    .endDate(loan.getEndDate() != null ? loan.getEndDate() : null)
                    .remainingAmount(loan.getRemainingAmount())
                    .lender(lender)
                    .receiver(receiver)
                    .payments(loan.getPayments())
                    .build());
        }
        return response;
    }

    public LoanDto getLoanByLoanId(Long loanId){
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        LenderDto lender = LenderDto.builder()
                .id(loan.getLender().getId())
                .name(loan.getLender().getFirstName() + loan.getLender().getLastName())
                .build();
        ReceiverDto receiver = ReceiverDto.builder()
                .id(loan.getReceiver().getId())
                .name(loan.getReceiver().getName())
                .contact(loan.getReceiver().getContact())
                .build();

        return LoanDto.builder()
                .loanId(loan.getId())
                .principalAmount(loan.getPrincipalAmount())
                .interestRate(loan.getInterestRate())
                .loanType(loan.getLoanType())
                .status(loan.getStatus())
                .startDate(loan.getStartDate())
                .endDate(loan.getEndDate() != null ? loan.getEndDate() : null)
                .remainingAmount(loan.getRemainingAmount())
                .lender(lender)
                .receiver(receiver)
                .payments(loan.getPayments())
                .build();
    }

    public LoanDto createLoan(LoanCreateRequest loanCreateRequest){

        User lender =  userRepository.findById(loanCreateRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Lender not found"));

        Receiver receiver =  receiverRepository.findById(loanCreateRequest.receiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        Loan loan = Loan.builder()
                .principalAmount(loanCreateRequest.principalAmount())
                .interestRate(loanCreateRequest.interestRate())
                .loanType(loanCreateRequest.loanType())
                .status(LoanStatus.ACTIVE)
                .startDate(loanCreateRequest.startDate())
                .endDate(loanCreateRequest.loanType().equals(LoanType.WEEKLY) ?
                        Utils.addTenWeeks(LocalDateTime.now()) :
                        loanCreateRequest.endDate() != null ? loanCreateRequest.endDate() : null)
                .remainingAmount(loanCreateRequest.principalAmount())
                .lender(lender)
                .receiver(receiver)
                .payments(new ArrayList<>())
                .build();

        var savedLoan = loanRepository.save(loan);

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

    public SuccessResponse updateLoan(LoanUpdateRequest loanUpdateRequest){

        Loan loan = loanRepository.findById(loanUpdateRequest.loanId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        loan.setStatus(loanUpdateRequest.status());
        loan.setEndDate(LocalDateTime.now());
        loanRepository.save(loan);
        return  SuccessResponse.builder()
                .message("Loan Closed successfully")
                .build();
    }

    public SuccessResponse deleteLoan(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        loanRepository.deleteById(loanId);

        return SuccessResponse.builder()
                .message("Loan Deleted Successfully")
                .build();
    }

    public SuccessResponse closeLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        loan.setStatus(LoanStatus.CLOSED);
        loanRepository.save(loan);

        return SuccessResponse.builder()
                .message("Loan Deleted Successfully")
                .build();
    }


//    private Receiver resolveReceiver(LoanCreateRequest loanCreateRequest, User lender){
//
//        if(loanCreateRequest.receiver().id() != null){
//            return  receiverRepository.findByIdAndLenderId(loanCreateRequest.receiver().id(),
//                    loanCreateRequest.userId()).orElseThrow(() -> new ResourceNotFoundException("Receiver not found for this lender"));
//        }
//
//        String receiverName = loanCreateRequest.receiver().name();
//        Long receiverContact = loanCreateRequest.receiver().contact();
//
//
//        if (loanCreateRequest.receiver().name() == null ||
//                loanCreateRequest.receiver().name().isBlank() ||
//                loanCreateRequest.receiver().contact() == null)
//            throw new IllegalArgumentException("receiver name is required when creating a new receiver");
//
//        return receiverRepository.save(
//                Receiver.builder()
//                        .name(receiverName)
//                        .contact(receiverContact)
//                        .loans(new HashSet<>())
//                        .lender(lender)
//                        .build());
//    }

}
