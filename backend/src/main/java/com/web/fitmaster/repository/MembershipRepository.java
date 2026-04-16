package com.web.fitmaster.repository;

import com.web.fitmaster.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional <Membership> findTopByMemberIdOrderByEndDateDesc(Long id);

    @Query("select sum (m.pkg.price) from Membership m where m.member.id=:id ")
    BigDecimal sumPackagePriceByMemberId(Long id);
    

    Optional<Membership> findTopByMemberIdOrderByStartDateDesc(Long memberId);



    List<Membership> findAllByMemberId(Long memberId);

    List<Membership> findMembershipByMemberIdOrderByEndDateDesc(Long memberId);

    @Query("select sum (m.pkg.price) from Membership m ")
    BigDecimal sumAllPrices();

    List<Membership> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDate start, LocalDate end);

    List<Membership> findByCreatedAtBetweenAndMemberGenderOrderByCreatedAtDesc(LocalDate start, LocalDate end, String gender);
    long countDistinctMemberByEndDateAfter(LocalDate date);
    long countDistinctMemberByEndDateBetween(LocalDate start, LocalDate end);
}
