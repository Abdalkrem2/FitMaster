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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/history")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<WorkoutPlanDTOs.WorkoutPlanResponse>> getHistory() {
        Long memberId = authUtil.loggedInUserId();
        return ResponseEntity.ok(
                workoutPlanService.getAllPlans(memberId).stream()
                        .map(workoutPlanMapper::toResponse)
                        .toList()
        );
    }

}
