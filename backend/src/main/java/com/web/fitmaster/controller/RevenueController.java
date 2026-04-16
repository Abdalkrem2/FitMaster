package com.web.fitmaster.controller;

import com.web.fitmaster.dto.RevenueDTOs;
import com.web.fitmaster.service.RevenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/revenue")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RevenueController {

    private final RevenueService revenueService;

@GetMapping("/stats")
    public ResponseEntity<RevenueDTOs.StatsResponse> getRevenueStats() {
    return ResponseEntity.ok(revenueService.getRevenueStats());
}

@GetMapping("/monthly")
    public ResponseEntity<RevenueDTOs.MonthlyResponse> getRevenueMonthly(@RequestParam(defaultValue = "2026") int year) {
return ResponseEntity.ok(revenueService.getRevenueMonthly(year));
}

@GetMapping("/period")
    public ResponseEntity<RevenueDTOs.PeriodResponse> getRevenueByPeriod(@RequestParam LocalDate start,
                                                          @RequestParam LocalDate end,@RequestParam(defaultValue = "All") String gender) {
    return ResponseEntity.ok(revenueService.getRevenueByPeriod(start,end,gender));

}



}
