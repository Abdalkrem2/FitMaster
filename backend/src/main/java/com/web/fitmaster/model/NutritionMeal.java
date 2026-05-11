// NutritionMeal.java
package com.web.fitmaster.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "nutrition_meals")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class NutritionMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private NutritionPlan nutritionPlan;

    @Column(nullable = false)
    private String name;

    @Column(name = "meal_time", nullable = false)
    private String mealTime;

    @Column(name = "prep_time")
    private String prepTime;

    @Column(name = "total_calories", nullable = false)
    private Integer totalCalories;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NutritionFood> foods;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NutritionRecipeStep> recipeSteps;
}