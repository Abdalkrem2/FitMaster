package com.web.fitmaster.event;

import com.web.fitmaster.model.ActivityLog;
import com.web.fitmaster.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivityLogListener {
private final ActivityLogRepository activityLogRepository;

@EventListener
@Async//means the log is saved in a separate thread — the API response doesn't wait for it.
public void handleActivityEvent(ActivityEvent event) {
ActivityLog activityLog = ActivityLog.builder()
        .entityType(event.getEntityType())
        .action(event.getAction())
        .performedBy(event.getPerformedBy())
        .details(event.getDetails())
        .entityId(event.getEntityId())
        .build();
activityLogRepository.save(activityLog);
}

}
