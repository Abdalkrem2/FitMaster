package com.web.fitmaster.dto;

import com.web.fitmaster.model.enums.ActionType;
import com.web.fitmaster.model.enums.EntityType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ActivityLogDTOs {
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
    public static class LogsDTO {
        private Long id;
        private ActionType actionType;
        private String performedByName;
        private EntityType entityType;
        private String details;
        private Long entityId;
        private LocalDateTime createdAt;

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LogsResponse {
        private List<LogsDTO> content;
        private Integer pageNumber;
        private Integer pageSize;
        private Long totalElements;
        private Integer totalPages;
        private Boolean LastPage;
    }


}
