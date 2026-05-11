package com.web.fitmaster.dto;
import com.web.fitmaster.model.enums.FitnessGoal;
import com.web.fitmaster.model.enums.FitnessLevel;
import com.web.fitmaster.model.enums.SplitType;
import com.web.fitmaster.model.enums.WorkoutPlanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
public class WorkoutPlanDTOs {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WorkoutPlanResponse {
        private Long id;
        private String name;
        private SplitType splitType;
        private FitnessGoal goal;
        private FitnessLevel level;
        private WorkoutPlanStatus status;
        private LocalDateTime createdAt;
        private List<WorkoutDayResponse> days;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WorkoutDayResponse {
        private Integer dayNumber;
        private String muscleGroupLabel;
        private List<WorkoutExerciseResponse> exercises;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WorkoutExerciseResponse {
        private Long id;
        private String exerciseName;
        private String imageUrl;
        private String instructions;
        private Integer sets;
        private Integer reps;
        private Integer repsMax;
        private Integer orderIndex;
    }

}
