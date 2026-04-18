package com.web.fitmaster.repository;

import com.web.fitmaster.model.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
    Optional<MemberProfile> findByMemberIdAndMember_IsActivatedTrueAndMember_DeletedFalse(Long memberId);

    Optional<MemberProfile> findByMemberId(Long id);
}
