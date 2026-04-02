package com.web.fitmaster.event;

import com.web.fitmaster.model.User;
import com.web.fitmaster.model.enums.ActionType;
import com.web.fitmaster.model.enums.EntityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ActivityEvent {
    private final ActionType action;
    private final EntityType entityType;
    private final Long entityId;
    private final User performedBy;
    private final String details;
}
