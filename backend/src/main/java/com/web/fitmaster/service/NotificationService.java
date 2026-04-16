package com.web.fitmaster.service;

import com.web.fitmaster.dto.NotificationDTOs;
import com.web.fitmaster.model.enums.NotificationType;
import org.springframework.data.domain.Pageable;

public interface NotificationService {


    void createNotification(NotificationType type, Long referenceId, String message, String details);
    // when you give user the admine role
    void assignNotificationsToUser(Long userId);

    NotificationDTOs.NotificationResponse getMyNotifications(Pageable pageable);

    void markAsRead(Long notificationId);
    void markAllAsRead();
    void deleteNotification(Long notificationId);
    void clearAll();
}