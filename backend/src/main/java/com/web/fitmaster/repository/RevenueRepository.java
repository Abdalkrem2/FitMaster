package com.web.fitmaster.repository;

import com.web.fitmaster.model.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {


    List<Revenue> findByCreatedAtBetween(LocalDate createdAtAfter, LocalDate createdAtBefore);

    @Query("select sum (r.amount) from Revenue r where r.member.id=:memberId")
    BigDecimal sumAmountByMemberId(Long memberId);


}
