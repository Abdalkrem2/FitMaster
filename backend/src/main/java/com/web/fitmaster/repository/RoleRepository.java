package com.web.fitmaster.repository;

import com.web.fitmaster.model.Role;
import com.web.fitmaster.model.enums.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(AppRole appRole);
}
