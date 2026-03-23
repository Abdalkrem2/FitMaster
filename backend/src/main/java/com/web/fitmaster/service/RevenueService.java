package com.web.fitmaster.service;

import com.web.fitmaster.dto.RevenueDTOs;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public interface RevenueService {

    RevenueDTOs.RevenueDTO createRevenue(@Valid RevenueDTOs.RevenueRequest revenueRequest);

    RevenueDTOs.RevenueResponse getRevenues(Pageable pageable);

    RevenueDTOs.RevenueDTO getRevenue(Long id);

    RevenueDTOs.RevenueDTO updateRevenue(Long id, RevenueDTOs.RevenueUpdateRequest revenueRequest);

    String deleteRevenue(Long id);

    List<RevenueDTOs.RevenueDTO> getRevenueByDateRange(LocalDate start, LocalDate end);
}
