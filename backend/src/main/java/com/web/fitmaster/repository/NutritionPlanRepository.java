package com.web.fitmaster.repository;

import com.web.fitmaster.model.NutritionPlan;
import com.web.fitmaster.model.enums.WorkoutPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
    Optional<NutritionPlan> findByMember_IdAndStatus(Long memberId, WorkoutPlanStatus status);
    List<NutritionPlan> findByMember_IdOrderByCreatedAtDesc(Long memberId);
}