package com.dev.my_finance.entity;

import com.dev.my_finance.enumeration.PaymentType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amountPaid;

    private LocalDateTime paymentDate;

    private PaymentType paymentType;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    @JsonBackReference
    private Loan loan;
}
