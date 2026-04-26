// NutritionFood.java
package com.web.fitmaster.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nutrition_foods")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class NutritionFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private NutritionMeal meal;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String amount;

    @Column(nullable = false)
    private Integer calories;

    @Column(name = "protein_grams", nullable = false)
    private Integer proteinGrams;

    @Column(name = "carbs_grams", nullable = false)
    private Integer carbsGrams;

    @Column(name = "fat_grams", nullable = false)
    private Integer fatGrams;
}