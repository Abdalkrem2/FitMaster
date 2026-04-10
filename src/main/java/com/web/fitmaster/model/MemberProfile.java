package com.web.fitmaster.model;

import com.web.fitmaster.model.enums.ActivityLevel;
import com.web.fitmaster.model.enums.FitnessLevel;
import com.web.fitmaster.model.enums.GoalPriority;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "member_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double weight;
    private Double height;
    private LocalDate dateOfBirth;

    private Double bodyFatPercentage;
    private Double leanBodyMass;

    @Enumerated(EnumType.STRING)
    private GoalPriority goalPriority;

    @Enumerated(EnumType.STRING)
    private FitnessLevel fitnessLevel;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        lastUpdated = LocalDateTime.now();
    }
}