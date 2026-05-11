package com.web.fitmaster.dto;

import com.web.fitmaster.model.enums.FitnessGoal;
import com.web.fitmaster.model.enums.FitnessLevel;
import com.web.fitmaster.model.enums.SplitType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class PdfWorkoutPlanDTO {

    @Getter
    @Builder
    public static class PdfPlanResponse {
        private String name;
        private SplitType splitType;
        private FitnessGoal goal;
        private FitnessLevel level;
        private List<PdfDayResponse> days;
    }

    @Getter
    @Builder
    public static class PdfDayResponse {
        private Integer dayNumber;
        private String muscleGroupLabel;
        private List<PdfExerciseResponse> exercises;
    }

    @Getter
    @Builder
    public static class PdfExerciseResponse {
        private Integer orderIndex;
        private String exerciseName;
        private String imageBase64;   // هنا فقط
        private String instructions;
        private Integer sets;
        private Integer reps;
        private Integer repsMax;
    }
}