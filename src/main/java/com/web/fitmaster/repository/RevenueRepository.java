package com.web.fitmaster.repository;

import com.web.fitmaster.model.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {


    List<Revenue> findByDate(LocalDate date);


    List<Revenue> findByDateBetween(LocalDate start, LocalDate end);


}
