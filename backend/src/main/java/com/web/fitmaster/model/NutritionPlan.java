
package com.web.fitmaster.model;

import com.web.fitmaster.model.enums.FitnessGoal;
import com.web.fitmaster.model.enums.WorkoutPlanStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "nutrition_plans")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class NutritionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FitnessGoal goal;

    @Column(name = "daily_calories", nullable = false)
    private Integer dailyCalories;

    @Column(name = "protein_grams", nullable = false)
    private Integer proteinGrams;

    @Column(name = "carbs_grams", nullable = false)
    private Integer carbsGrams;

    @Column(name = "fat_grams", nullable = false)
    private Integer fatGrams;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutPlanStatus status;

    @OneToMany(mappedBy = "nutritionPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NutritionMeal> meals;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}