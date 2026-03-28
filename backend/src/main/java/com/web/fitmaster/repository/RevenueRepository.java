package com.web.fitmaster.repository;

import com.web.fitmaster.dto.MonthlyRevenue;
import com.web.fitmaster.model.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {


    List<Revenue> findByCreatedAtBetween(LocalDate createdAtAfter, LocalDate createdAtBefore);

    @Query("select sum (r.amount) from Revenue r where r.member.id=:memberId")
    BigDecimal sumAmountByMemberId(Long memberId);

    @Query("select coalesce(sum(r.amount),0) from Revenue r where r.createdAt=:date")
    BigDecimal sumByDate(@Param("date") LocalDate date);

    @Query("select coalesce(sum(r.amount),0)from Revenue r where r.createdAt BETWEEN :start and :end ")
    BigDecimal sumBetweenDates(@Param("start") LocalDate start,@Param("end") LocalDate end);

    // Monthly breakdown — returns month number + total per month for a given year
    @Query("SELECT MONTH(r.createdAt) AS month, COALESCE(SUM(r.amount), 0) AS total " +
            "FROM Revenue r WHERE YEAR(r.createdAt) = :year " +
            "GROUP BY MONTH(r.createdAt)")
    List<MonthlyRevenue> monthlyBreakdown(@Param("year") int year);


    @Query("select sum (r.amount) from Revenue r")
    BigDecimal sumAllAmounts();


    List<Revenue> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDate start, LocalDate end);

    List<Revenue> findByCreatedAtBetweenAndMemberGenderOrderByCreatedAtDesc(
            LocalDate start, LocalDate end, String gender);
}
