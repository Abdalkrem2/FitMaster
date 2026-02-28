package com.web.fitmaster.service.Imp;

import com.web.fitmaster.model.Revenue;
import com.web.fitmaster.repository.RevenueRepository;
import com.web.fitmaster.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RevenueServiceImpl implements RevenueService {
    private final RevenueRepository revenueRepository;

    @Autowired
    public RevenueServiceImpl(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }

    @Override
    public Revenue saveRevenue(Revenue revenue) {
        return revenueRepository.save(revenue);
    }

    @Override
    public Revenue saveRevenue(Revenue revenue, Long userId) {
        return null;
    }

    @Override
    public List<Revenue> getAllRevenue() {
        return revenueRepository.findAll();
    }

    @Override
    public List<Revenue> getRevenueByDate(LocalDate date) {
        return revenueRepository.findByDate(date);
    }

    @Override
    public List<Revenue> getRevenueByDateRange(LocalDate start, LocalDate end) {
        return revenueRepository.findByDateBetween(start, end);
    }

    @Override
    public void deleteRevenue(Long id) {
        revenueRepository.deleteById(id);
    }
}
