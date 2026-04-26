// NutritionRecipeStep.java
package com.web.fitmaster.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nutrition_recipe_steps")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class NutritionRecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private NutritionMeal meal;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String instruction;
}