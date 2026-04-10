package com.web.fitmaster.repository;

import com.web.fitmaster.model.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {


    Optional<MemberProfile> findByUserId(Long userId);

}