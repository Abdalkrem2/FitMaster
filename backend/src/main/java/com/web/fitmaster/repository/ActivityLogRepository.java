package com.web.fitmaster.repository;

import com.web.fitmaster.model.ActivityLog;
import com.web.fitmaster.model.enums.EntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    Page<ActivityLog> findByEntityTypeOrderByCreatedAtDesc(EntityType entityType, Pageable pageable);

    Page<ActivityLog> findByPerformedBy_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<ActivityLog> findByEntityTypeAndPerformedBy_IdOrderByCreatedAtDesc(EntityType entityType, Long preformedById, Pageable pageable);

    Page<ActivityLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
