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
@RequestMapping("/api/revenues")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RevenueDTOs.RevenueResponse> getAllRevenues(Pageable pageable) {
        return ResponseEntity.ok(revenueService.getRevenues(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RevenueDTOs.RevenueDTO> getRevenue(@PathVariable Long id) {
        return ResponseEntity.ok(revenueService.getRevenue(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RevenueDTOs.RevenueDTO> createRevenue(@Valid @RequestBody RevenueDTOs.RevenueRequest request) {
        return new ResponseEntity<>(revenueService.createRevenue(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RevenueDTOs.RevenueDTO> updateRevenue(
            @PathVariable Long id,
            @RequestBody RevenueDTOs.RevenueUpdateRequest request) {
        return ResponseEntity.ok(revenueService.updateRevenue(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRevenue(@PathVariable Long id) {
        return ResponseEntity.ok(revenueService.deleteRevenue(id));
    }

    @GetMapping("/range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RevenueDTOs.RevenueDTO>> getRevenueByRange(
            @RequestParam("start") LocalDate start,
            @RequestParam("end") LocalDate end) {
        return ResponseEntity.ok(revenueService.getRevenueByDateRange(start, end));
    }
}
