package com.web.fitmaster.model;

import com.fitmaster.model.enums.PaymentMethod;
import com.web.fitmaster.model.User;
import jakarta.persistence.*;
import com.fitmaster.model.enums.RevenueType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
 @NoArgsConstructor
 @AllArgsConstructor
 @Getter
 @Setter
@Entity
@Table( name = "revenues")
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RevenueType revenueType;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;





}



