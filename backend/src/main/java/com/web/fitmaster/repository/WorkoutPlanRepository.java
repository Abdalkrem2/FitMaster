package com.web.fitmaster.repository;

import com.web.fitmaster.model.WorkoutPlan;
import com.web.fitmaster.model.enums.WorkoutPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    Optional<WorkoutPlan> findByMember_IdAndStatus(Long memberId, WorkoutPlanStatus status);

    List<WorkoutPlan> findByMember_IdOrderByCreatedAtDesc(Long memberId);


    List<WorkoutPlan> findByMember_Id(Long memberId);
}
