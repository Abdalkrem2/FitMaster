package com.web.fitmaster.nutrition;

import com.web.fitmaster.dto.NutritionPlanDTOs.*;
import com.web.fitmaster.model.*;
import org.springframework.stereotype.Component;

@Component
public class NutritionPlanMapper {

    public NutritionPlanResponse toResponse(NutritionPlan plan) {
        return NutritionPlanResponse.builder()
                .id(plan.getId())
                .goal(plan.getGoal())
                .dailyCalories(plan.getDailyCalories())
                .proteinGrams(plan.getProteinGrams())
                .carbsGrams(plan.getCarbsGrams())
                .fatGrams(plan.getFatGrams())
                .status(plan.getStatus())
                .createdAt(plan.getCreatedAt())
                .meals(plan.getMeals().stream()
                        .map(this::toMealResponse)
                        .toList())
                .build();
    }

    private NutritionMealResponse toMealResponse(NutritionMeal meal) {
        return NutritionMealResponse.builder()
                .id(meal.getId())
                .name(meal.getName())
                .mealTime(meal.getMealTime())
                .prepTime(meal.getPrepTime())
                .totalCalories(meal.getTotalCalories())
                .foods(meal.getFoods().stream()
                        .map(this::toFoodResponse)
                        .toList())
                .recipeSteps(meal.getRecipeSteps().stream()
                        .map(this::toStepResponse)
                        .toList())
                .build();
    }

    private NutritionFoodResponse toFoodResponse(NutritionFood food) {
        return NutritionFoodResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .amount(food.getAmount())
                .calories(food.getCalories())
                .proteinGrams(food.getProteinGrams())
                .carbsGrams(food.getCarbsGrams())
                .fatGrams(food.getFatGrams())
                .build();
    }

    private NutritionRecipeStepResponse toStepResponse(NutritionRecipeStep step) {
        return NutritionRecipeStepResponse.builder()
                .id(step.getId())
                .stepOrder(step.getStepOrder())
                .instruction(step.getInstruction())
                .build();
    }
}