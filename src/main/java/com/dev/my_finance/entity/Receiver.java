package com.dev.my_finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Receiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String contact;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Loan> loans = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender;
}
