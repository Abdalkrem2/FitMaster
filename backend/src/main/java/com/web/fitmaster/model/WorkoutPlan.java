package com.web.fitmaster.model;

import com.web.fitmaster.model.enums.FitnessGoal;
import com.web.fitmaster.model.enums.FitnessLevel;
import com.web.fitmaster.model.enums.SplitType;
import com.web.fitmaster.model.enums.WorkoutPlanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "workout_plans")
public class WorkoutPlan {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FitnessGoal goal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FitnessLevel level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutPlanStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "split_type", nullable = false)
    private SplitType splitType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WorkoutDay> workoutDays;

    @Column(name = "plan_start_date")
    private LocalDateTime planStartDate;

    @Column(name = "plan_end_date")
    private LocalDateTime planEndDate;

    @Column(name = "completed_days_count", columnDefinition = "INT DEFAULT 0")
    private Integer completedDaysCount = 0;

    @Column(name = "total_days_in_plan")
    private Integer totalDaysInPlan;

    @Transient
    private Boolean isWeekEnded = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (planStartDate == null) {
            planStartDate = LocalDateTime.now();
        }
        if (totalDaysInPlan != null && planEndDate == null) {
            // Set end date to next Saturday 23:59
            planEndDate = calculateNextSaturdayEnd();
        }
        if (completedDaysCount == null) {
            completedDaysCount = 0;
        }
    }

    private LocalDateTime calculateNextSaturdayEnd() {
        LocalDateTime now = LocalDateTime.now();
        // Find next Saturday (day of week 6)
        int daysUntilSaturday = (6 - now.getDayOfWeek().getValue() + 7) % 7;
        if (daysUntilSaturday == 0) {
            daysUntilSaturday = 7; // If today is Saturday, go to next Saturday
        }
        return now.plusDays(daysUntilSaturday).withHour(23).withMinute(59).withSecond(0);
    }


}
