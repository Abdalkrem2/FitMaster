package com.web.fitmaster.repository;

import com.web.fitmaster.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional <Membership> findTopByMemberIdOrderByEndDateDesc(Long id);

    @Query("select sum (m.pkg.price) from Membership m where m.member.id=:id ")
    BigDecimal sumPackagePriceByMemberId(Long id);
    

    Optional<Membership> findTopByMemberIdOrderByStartDateDesc(Long memberId);

    List<Membership> findMembershipByMemberId(Long id);

    List<Membership> findAllByMemberId(Long memberId);

    List<Membership> findMembershipByMemberIdOrderByEndDateDesc(Long memberId);
}
