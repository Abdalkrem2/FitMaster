package com.web.fitmaster.model;

import com.web.fitmaster.model.enums.ActionType;
import com.web.fitmaster.model.enums.EntityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "activity_logs"
//indexes = {
//@Index(name="idx_log_user", columnList="user_id"),
//@Index(name="idx_log_created", columnList="created_at"),
//@Index(name="idx_log_user_created", columnList="user_id,created_at")
//  }
  )
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preformed_by", nullable = false)
    private User preformedBy;

    @Column(nullable = false,length = 50)
    @Enumerated(EnumType.STRING)
    private ActionType action;

    @Column(length=50)
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(nullable = false)
    private Long entityId;

    private String details;

    @Column(name="created_at",nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }




}
