package com.web.fitmaster.service;

import com.web.fitmaster.model.Revenue;

import java.time.LocalDate;
import java.util.List;

public interface RevenueService {

    Revenue saveRevenue(Revenue revenue);

    Revenue saveRevenue(Revenue revenue, Long userId);

    List<Revenue> getAllRevenue();

    List<Revenue> getRevenueByDate(LocalDate date);

    List<Revenue> getRevenueByDateRange(LocalDate start, LocalDate end);

    void deleteRevenue(Long id);
}
