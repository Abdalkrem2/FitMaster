package com.web.fitmaster.repository;

import com.web.fitmaster.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<User,Long> {

}
