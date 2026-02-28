package com.web.fitmaster.controller;

import com.web.fitmaster.dto.RevenueDTO;
import com.web.fitmaster.model.Revenue;
import com.web.fitmaster.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/revenues")
public class RevenueController {

    private final RevenueService revenueService;

    @Autowired
    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }


    @PostMapping
    public Revenue addRevenue(@RequestBody RevenueDTO dto) {
        Revenue revenue = new Revenue();
        revenue.setAmount(dto.getAmount());
        revenue.setDate(dto.getDate());
        revenue.setRevenueType(dto.getRevenueType());
        revenue.setPaymentMethod(dto.getPaymentMethod());
        revenue.setDescription(dto.getDescription());

        return revenueService.saveRevenue(revenue, dto.getUserId());
    }



    @GetMapping
    public List<Revenue> getAllRevenue() {
        return revenueService.getAllRevenue();
    }


    @GetMapping("/date")
    public List<Revenue> getRevenueByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return revenueService.getRevenueByDate(date);
    }


    @GetMapping("/range")
    public List<Revenue> getRevenueByRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return revenueService.getRevenueByDateRange(start, end);
    }


    @DeleteMapping("/{id}")
    public void deleteRevenue(@PathVariable Long id) {
        revenueService.deleteRevenue(id);
    }
}
