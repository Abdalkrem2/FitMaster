package com.web.fitmaster.controller;

import com.web.fitmaster.dto.NutritionPlanDTOs;
import com.web.fitmaster.dto.NutritionPlanDTOs.NutritionPlanResponse;
import com.web.fitmaster.model.NutritionPlan;
import com.web.fitmaster.service.Imp.NutritionPdfService;
import com.web.fitmaster.service.Imp.NutritionPlanService;
import com.web.fitmaster.util.AuthUtil;
import com.web.fitmaster.nutrition.NutritionPlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutrition-plans")
@RequiredArgsConstructor
public class NutritionPlanController {

    private final NutritionPlanService nutritionPlanService;
    private final NutritionPlanMapper nutritionPlanMapper;
    private final NutritionPdfService nutritionPdfService;
    private final AuthUtil authUtil;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<NutritionPlanResponse> generate() {
        Long memberId = authUtil.loggedInUserId();
        NutritionPlan plan = nutritionPlanService.generatePlan(memberId);
        return ResponseEntity.ok(nutritionPlanMapper.toResponse(plan));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<NutritionPlanResponse> getActive() {
        Long memberId = authUtil.loggedInUserId();
        NutritionPlanResponse plan = nutritionPlanService.getActivePlan(memberId);
        return ResponseEntity.ok(plan);
    }

    @GetMapping("/active/pdf")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<byte[]> downloadPdf() {
        Long memberId = authUtil.loggedInUserId();
        NutritionPlanResponse plan = nutritionPlanService.getActivePlan(memberId);
      ;

        byte[] pdf = nutritionPdfService.generateNutritionPlanPdf(plan);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=nutrition-plan.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<NutritionPlanResponse>> getHistory() {
        Long memberId = authUtil.loggedInUserId();
        return ResponseEntity.ok(
                nutritionPlanService.getAllPlans(memberId)
        );
    }
}