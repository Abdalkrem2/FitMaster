package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.NotificationDTOs;
import com.web.fitmaster.exceptions.NotFoundException;
import com.web.fitmaster.model.Notification;
import com.web.fitmaster.model.NotificationUserState;
import com.web.fitmaster.model.User;
import com.web.fitmaster.model.enums.AppRole;
import com.web.fitmaster.model.enums.NotificationType;
import com.web.fitmaster.repository.NotificationRepository;
import com.web.fitmaster.repository.NotificationUserStateRepository;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.service.NotificationService;
import com.web.fitmaster.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationUserStateRepository stateRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Override
    @Transactional
    public void createNotification(NotificationType type, Long referenceId, String message, String details) {
        // 1..save the notification
        Notification notification = Notification.builder()
                .type(type)
                .referenceId(referenceId)
                .message(message)
                .details(details)
                .build();
        notificationRepository.save(notification);

        // 2.get all admins the notifications
        List<User> admins = userRepository.findByRoles_roleNameInAndDeletedFalse(Set.of(AppRole.ADMIN));        LocalDateTime now = LocalDateTime.now();

        List<NotificationUserState> states = admins.stream()
                .map(admin -> NotificationUserState.builder()
                        .notification(notification)
                        .user(admin)
                        .read(false)
                        .deleted(false)
                        .assignedAt(admin.getAdminRoleAssignedAt() != null
                                ? admin.getAdminRoleAssignedAt()
                                : LocalDateTime.MIN)
                        .build())
                .toList();

        stateRepository.saveAll(states);
    }

    @Override
    @Transactional
    public void assignNotificationsToUser(Long userId) {
        // callee when give user the admin role
        // save the time when he takes the admin role to give him the new notification only hehehe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setAdminRoleAssignedAt(LocalDateTime.now());
        userRepository.save(user);


    }



    @Override
    public NotificationDTOs.NotificationResponse getMyNotifications(Pageable pageable) {
        User currentUser = authUtil.loggedInUser();
        LocalDateTime assignedAt = currentUser.getAdminRoleAssignedAt() != null
                ? currentUser.getAdminRoleAssignedAt()
                : LocalDateTime.of(2000, 1, 1, 0, 0, 0);//كانت عندي هون مشكلة بتخزين التاريخ غلط
        //LocalDateTime.MIN هاي غلط  لانه بترجع -999999999-01-01T00:00:00

        Page<NotificationUserState> states = stateRepository
                .findActiveByUserId(currentUser.getId(), assignedAt, pageable);

        long unreadCount = stateRepository.countUnread(
                currentUser.getId(), assignedAt);

        List<NotificationDTOs.NotificationDTO> content = states.stream()
                .map(this::mapToDTO)
                .toList();

        return NotificationDTOs.NotificationResponse.builder()
                .content(content)
                .pageNumber(states.getNumber())
                .pageSize(states.getSize())
                .totalElements(states.getTotalElements())
                .totalPages(states.getTotalPages())
                .lastPage(states.isLast())
                .unreadCount(unreadCount)
                .build();
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        User currentUser = authUtil.loggedInUser();
        NotificationUserState state = stateRepository
                .findByNotificationIdAndUserId(notificationId, currentUser.getId())
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        state.setRead(true);
        stateRepository.save(state);
    }

    @Override
    @Transactional
    public void markAllAsRead() {
        User currentUser = authUtil.loggedInUser();

        LocalDateTime assignedAt = currentUser.getAdminRoleAssignedAt() != null
                ? currentUser.getAdminRoleAssignedAt()
                : LocalDateTime.MIN;

        stateRepository.markAllAsRead(currentUser.getId(), assignedAt);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        User currentUser = authUtil.loggedInUser();
        NotificationUserState state = stateRepository
                .findByNotificationIdAndUserId(notificationId, currentUser.getId())
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        state.setDeleted(true);
        stateRepository.save(state);
    }

    @Override
    @Transactional
    public void clearAll() {
        User currentUser = authUtil.loggedInUser();
        stateRepository.deleteAllByUserId(currentUser.getId());
    }

    private NotificationDTOs.NotificationDTO mapToDTO(NotificationUserState state) {
        return NotificationDTOs.NotificationDTO.builder()
                .id(state.getNotification().getId())
                .type(state.getNotification().getType())
                .referenceId(state.getNotification().getReferenceId())
                .message(state.getNotification().getMessage())
                .details(state.getNotification().getDetails())
                .read(state.isRead())
                .createdAt(state.getNotification().getCreatedAt())
                .build();
    }
}