package com.web.fitmaster.controller;

import com.web.fitmaster.dto.NutritionPlanDTOs.NutritionPlanResponse;
import com.web.fitmaster.model.NutritionPlan;
import com.web.fitmaster.service.Imp.NutritionPlanService;
import com.web.fitmaster.util.AuthUtil;
import com.web.fitmaster.nutrition.NutritionPlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nutrition-plans")
@RequiredArgsConstructor
public class NutritionPlanController {

    private final NutritionPlanService nutritionPlanService;
    private final NutritionPlanMapper nutritionPlanMapper;
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
        NutritionPlan plan = nutritionPlanService.getActivePlan(memberId);
        return ResponseEntity.ok(nutritionPlanMapper.toResponse(plan));
    }
}