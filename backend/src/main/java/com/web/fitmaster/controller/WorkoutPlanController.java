package com.web.fitmaster.controller;

import com.web.fitmaster.dto.WorkoutPlanDTOs;
import com.web.fitmaster.model.WorkoutPlan;
import com.web.fitmaster.service.Imp.PdfService;
import com.web.fitmaster.service.Imp.WorkoutPlanService;
import com.web.fitmaster.util.AuthUtil;
import com.web.fitmaster.workout.WorkoutPlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {
    private final WorkoutPlanService workoutPlanService;
    private final WorkoutPlanMapper workoutPlanMapper;
    private final AuthUtil authUtil;
    private final PdfService pdfService;


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

    @PostMapping("/{planId}/renew")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<WorkoutPlanDTOs.WorkoutPlanResponse> renewPlan(
            @PathVariable Long planId,
            @RequestBody WorkoutPlanDTOs.PlanRenewalRequest request) {
        Long memberId = authUtil.loggedInUserId();
        // Verify ownership
        WorkoutPlan plan = workoutPlanService.getActivePlan(memberId);
        if (!plan.getId().equals(planId)) {
            return ResponseEntity.status(403).build();
        }

        WorkoutPlan renewedPlan = workoutPlanService.renewPlan(
                planId,
                request.getChoice(),
                request.getNewSplit() != null ? request.getNewSplit().name() : null
        );
        return ResponseEntity.ok(workoutPlanMapper.toResponse(renewedPlan));
    }

    @PostMapping("/{planId}/days/{dayNumber}/complete")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Void> markDayCompleted(
            @PathVariable Long planId,
            @PathVariable Integer dayNumber) {
        Long memberId = authUtil.loggedInUserId();
        // Verify ownership
        WorkoutPlan plan = workoutPlanService.getActivePlan(memberId);
        if (!plan.getId().equals(planId)) {
            return ResponseEntity.status(403).build();
        }

        workoutPlanService.markDayCompleted(planId, dayNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active/pdf")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<byte[]> downloadPdf() {
        Long memberId = authUtil.loggedInUserId();
        WorkoutPlan plan = workoutPlanService.getActivePlan(memberId);
        WorkoutPlanDTOs.WorkoutPlanResponse response = workoutPlanMapper.toResponse(plan);

        byte[] pdf = pdfService.generateWorkoutPlanPdf(response);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=workout-plan.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
