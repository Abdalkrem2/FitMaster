package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.ActivityLogDTOs;
import com.web.fitmaster.model.ActivityLog;
import com.web.fitmaster.model.enums.EntityType;
import com.web.fitmaster.repository.ActivityLogRepository;
import com.web.fitmaster.service.ActivityLogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImp implements ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    @Override
    @Transactional
    public ActivityLogDTOs.LogsResponse getLogs(Long performedBy, EntityType entityType, Pageable pageable) {
        Page<ActivityLog>activityLogs;
        if(performedBy != null &&entityType != null) {
            activityLogs=activityLogRepository.findByEntityTypeAndPerformedBy_IdOrderByCreatedAtDesc(entityType, performedBy, pageable);
        }else if(performedBy != null) {
            activityLogs=activityLogRepository.findByPerformedBy_IdOrderByCreatedAtDesc(performedBy, pageable);
        }else if(entityType != null) {
            activityLogs=activityLogRepository.findByEntityTypeOrderByCreatedAtDesc(entityType, pageable);
        }else{
            activityLogs=activityLogRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        ActivityLogDTOs.LogsResponse logsResponse = new ActivityLogDTOs.LogsResponse().builder()
                .content(activityLogs.stream().map(this::mapToDTO).collect(Collectors.toList()))
                .pageSize(activityLogs.getSize())
                .pageNumber(activityLogs.getNumber())
                .totalElements(activityLogs.getTotalElements())
                .totalPages(activityLogs.getTotalPages())
                .LastPage(activityLogs.isLast())
                .totalPages(activityLogs.getTotalPages())
                .build();

        return logsResponse;
    }

    private ActivityLogDTOs.LogsDTO mapToDTO(ActivityLog activityLog) {
        return new ActivityLogDTOs.LogsDTO().builder()
                .id(activityLog.getLogId())
                .createdAt(activityLog.getCreatedAt())
                .entityType(activityLog.getEntityType())
                .performedByName(activityLog.getPerformedBy().getFullName())
                .actionType(activityLog.getAction())
                .entityId(activityLog.getEntityId())
                .details(activityLog.getDetails())
                .build();
    }
}
