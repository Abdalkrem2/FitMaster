package com.web.fitmaster.repository;

import com.web.fitmaster.model.Role;
import com.web.fitmaster.model.User;

import com.web.fitmaster.model.enums.AppRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByPhoneAndDeletedFalse(String phone);
    boolean existsByPhoneAndDeletedFalse(String phone);



    Optional<User> findByIdAndRoles_RoleNameInAndDeletedFalse(Long id, Set<AppRole> employee);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName IN :roles " +
            "AND u.deleted=false "+
            "AND (LOWER(u.fullName) LIKE LOWER(CONCAT('%',:search,'%')) " +
            "OR u.phone LIKE CONCAT('%',:search,'%'))")
    Page<User> searchMembers(@Param("roles") Set<AppRole> roles,@Param("search") String search, Pageable pageable);

    Page<User> findByRoles_roleNameInAndDeletedFalse(Set<AppRole> employee, Pageable pageable);
    List<User> findByRoles_roleNameInAndDeletedFalse(Set<AppRole> roles);
}
