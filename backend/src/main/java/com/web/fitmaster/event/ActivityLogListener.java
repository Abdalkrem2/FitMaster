package com.web.fitmaster.event;

import com.web.fitmaster.model.ActivityLog;
import com.web.fitmaster.model.enums.NotificationType;
import com.web.fitmaster.repository.ActivityLogRepository;
import com.web.fitmaster.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ActivityLogListener {
private final ActivityLogRepository activityLogRepository;
private final NotificationService notificationService;

@Async//means the log is saved in a separate thread — the API response doesn't wait for it.
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void handleActivityEvent(ActivityEvent event) {
ActivityLog activityLog = ActivityLog.builder()
        .entityType(event.getEntityType())
        .action(event.getAction())
        .performedBy(event.getPerformedBy())
        .details(event.getDetails())
        .entityId(event.getEntityId())
        .build();
ActivityLog savedLog = activityLogRepository.save(activityLog);

    //create notification automatically
    String message = event.getPerformedBy().getFullName()
            + " " + event.getAction().getLabel()
            + " a " + event.getEntityType().name().toLowerCase();

    notificationService.createNotification(
            NotificationType.ACTIVITY_LOG,
            savedLog.getLogId(),
            message,
            event.getDetails()
    );
}

}
