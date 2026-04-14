package com.web.fitmaster.model;

import com.web.fitmaster.model.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private Double targetWeight;

    @Enumerated(EnumType.STRING)
    private GoalPriority goalPriority;

    @Enumerated(EnumType.STRING)
    private FitnessLevel fitnessLevel;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    private Double experienceYears;
    private Integer trainingDaysPerWeek;

    @Enumerated(EnumType.STRING)
    private TrainingStyle preferredTrainingStyle;

    @Enumerated(EnumType.STRING)
    private MobilityLevel mobilityLevel;

    private Double sleepHours;

    @Enumerated(EnumType.STRING)
    private SleepQuality sleepQuality;

    @Enumerated(EnumType.STRING)
    private StressLevel stressLevel;

    @ElementCollection
    @CollectionTable(name = "member_injuries", joinColumns = @JoinColumn(name = "member_profile_id"))
    @Column(name = "injury_type")
    private List<String> injuries;

    @ElementCollection
    @CollectionTable(name = "member_allergies", joinColumns = @JoinColumn(name = "member_profile_id"))
    @Column(name = "allergy_type")
    private List<String> allergies;

    private Integer mealFrequency;

    @ElementCollection
    @CollectionTable(name = "member_disliked_foods", joinColumns = @JoinColumn(name = "member_profile_id"))
    @Column(name = "food_name")
    private List<String> dislikedFoods;

    @Enumerated(EnumType.STRING)
    private DietType dietType;

    @Enumerated(EnumType.STRING)
    private BudgetLevel foodBudgetLevel;

    private Boolean hasDiabetes;
    private Boolean hasHeartConditions;
    private Boolean hasHypertension;

    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        lastUpdated = LocalDateTime.now();
    }
}
