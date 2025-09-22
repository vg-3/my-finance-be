package com.dev.my_finance.entity;

import com.dev.my_finance.enumeration.LoanStatus;
import com.dev.my_finance.enumeration.LoanType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double principalAmount;

    private float interestRate;

    private LoanType loanType;

    private LoanStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double remainingAmount;

    @ManyToOne
    @JoinColumn(name = "lender_id")
    private User lender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Receiver receiver;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Payment> payments =  new ArrayList<>();
}
