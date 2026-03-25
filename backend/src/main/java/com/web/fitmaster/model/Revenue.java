package com.web.fitmaster.model;

import com.web.fitmaster.model.enums.PaymentMethod;
import com.web.fitmaster.model.User;
import jakarta.persistence.*;
import com.web.fitmaster.model.enums.RevenueType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table( name = "revenues")
@Builder
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = true)
    private User createdBy;

    @Column(nullable = false)
    private BigDecimal amount;

//    @Column(nullable = false,name = "payment_date")
//    private LocalDate Date;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private RevenueType revenueType;

    private String description;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private PaymentMethod paymentMethod;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }





}