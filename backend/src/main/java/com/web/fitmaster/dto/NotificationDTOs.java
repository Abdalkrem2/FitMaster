package com.web.fitmaster.dto;

import com.web.fitmaster.model.enums.NotificationType;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

public class NotificationDTOs {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationDTO {
        private Long id;
        private NotificationType type;
        private Long referenceId;
        private String message;
        private String details;
        private boolean read;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationResponse {
        private List<NotificationDTO> content;
        private Integer pageNumber;
        private Integer pageSize;
        private Long totalElements;
        private Integer totalPages;
        private Boolean lastPage;
        private Long unreadCount;
    }
}