package com.web.fitmaster.repository;

import com.web.fitmaster.model.ActivityLog;
import com.web.fitmaster.model.enums.EntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    Page<ActivityLog> findByEntityType(EntityType entityType, Pageable pageable);

    Page<ActivityLog> findByPreformedBy_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<ActivityLog> findByEntityTypeAndPreformedBy_IdOrderByCreatedAtDesc(EntityType entityType, Long preformedById, Pageable pageable);

    Page<ActivityLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
