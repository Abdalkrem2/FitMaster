package com.web.fitmaster.repository;

import com.web.fitmaster.model.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
    @Query("""
    SELECT mp FROM MemberProfile mp
    LEFT JOIN FETCH mp.injuries
    WHERE mp.memberId = :memberId
    AND mp.member.isActivated = true
    AND mp.member.deleted = false
""")
    Optional<MemberProfile> findByMemberIdAndMember_IsActivatedTrueAndMember_DeletedFalse(
            @Param("memberId") Long memberId
    );
    @Query("""
    SELECT mp FROM MemberProfile mp
    LEFT JOIN FETCH mp.injuries
    WHERE mp.memberId = :id
""")
    Optional<MemberProfile> findByMemberId(@Param("id") Long id);

    @Query("""
    SELECT mp FROM MemberProfile mp
    LEFT JOIN FETCH mp.allergies
    WHERE mp.memberId = :memberId
    AND mp.member.isActivated = true
    AND mp.member.deleted = false
""")
    Optional<MemberProfile> findByMemberIdWithAllergies(
            @Param("memberId") Long memberId
    );
}
