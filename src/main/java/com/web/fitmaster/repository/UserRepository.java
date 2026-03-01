package com.web.fitmaster.repository;

import com.web.fitmaster.model.Role;
import com.web.fitmaster.model.User;

import com.web.fitmaster.model.enums.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByPhone(String phone);
    boolean existsByPhone(String phone);



    Optional<User> findByIdAndRoles_RoleNameIn(Long id, Set<AppRole> employee);
}
