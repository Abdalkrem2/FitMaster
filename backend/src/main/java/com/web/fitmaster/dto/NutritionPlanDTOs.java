package com.web.fitmaster.dto;

import com.web.fitmaster.model.enums.FitnessGoal;
import com.web.fitmaster.model.enums.WorkoutPlanStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

public class NutritionPlanDTOs {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class NutritionPlanResponse {
        private Long id;
        private FitnessGoal goal;
        private Integer dailyCalories;
        private Integer proteinGrams;
        private Integer carbsGrams;
        private Integer fatGrams;
        private WorkoutPlanStatus status;
        private LocalDateTime createdAt;
        private List<NutritionMealResponse> meals;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class NutritionMealResponse {
        private Long id;
        private String name;
        private String mealTime;
        private String prepTime;
        private Integer totalCalories;
        private List<NutritionFoodResponse> foods;
        private List<NutritionRecipeStepResponse> recipeSteps;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class NutritionFoodResponse {
        private Long id;
        private String name;
        private String amount;
        private Integer calories;
        private Integer proteinGrams;
        private Integer carbsGrams;
        private Integer fatGrams;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class NutritionRecipeStepResponse {
        private Long id;
        private Integer stepOrder;
        private String instruction;
    }
}