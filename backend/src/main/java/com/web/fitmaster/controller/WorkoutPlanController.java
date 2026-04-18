package com.web.fitmaster.controller;

import com.web.fitmaster.dto.WorkoutPlanDTOs;
import com.web.fitmaster.model.WorkoutPlan;
import com.web.fitmaster.service.Imp.WorkoutPlanService;
import com.web.fitmaster.util.AuthUtil;
import com.web.fitmaster.workout.WorkoutPlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {
    private final WorkoutPlanService workoutPlanService;
    private final WorkoutPlanMapper workoutPlanMapper;
    private final AuthUtil authUtil;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<WorkoutPlanDTOs.WorkoutPlanResponse> generate() {
        Long memberId = authUtil.loggedInUserId();
        WorkoutPlan plan = workoutPlanService.generatePlan(memberId);
        return ResponseEntity.ok(workoutPlanMapper.toResponse(plan));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<WorkoutPlanDTOs.WorkoutPlanResponse> getActive() {
        Long memberId = authUtil.loggedInUserId();
        WorkoutPlan plan = workoutPlanService.getActivePlan(memberId);
        return ResponseEntity.ok(workoutPlanMapper.toResponse(plan));
    }

}
