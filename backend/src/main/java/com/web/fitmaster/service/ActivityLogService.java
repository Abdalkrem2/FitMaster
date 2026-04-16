package com.web.fitmaster.service;

import com.web.fitmaster.dto.ActivityLogDTOs;
import com.web.fitmaster.model.enums.EntityType;
import org.springframework.data.domain.Pageable;

public interface ActivityLogService {
    ActivityLogDTOs.LogsResponse getLogs(Long performedBy, EntityType entityType, Pageable pageable);
}
