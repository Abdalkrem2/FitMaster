package com.web.fitmaster.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_user_state",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"notification_id", "user_id"})
        }
)
public class NotificationUserState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    // ✅ filter the time that user(admin) take the admin-rolee
    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;
}