package com.web.fitmaster.service;

import com.web.fitmaster.dto.RevenueDTOs;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public interface RevenueService {

    RevenueDTOs.StatsResponse getRevenueStats();

    RevenueDTOs.MonthlyResponse getRevenueMonthly(int year);

    RevenueDTOs.PeriodResponse getRevenueByPeriod(LocalDate start, LocalDate end, String gender);
}
