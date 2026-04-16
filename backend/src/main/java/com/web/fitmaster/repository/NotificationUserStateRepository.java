package com.web.fitmaster.repository;

import com.web.fitmaster.model.NotificationUserState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NotificationUserStateRepository
        extends JpaRepository<NotificationUserState, Long> {

    // get all notification (after assignedAd + not deleted)
    @Query("""
        SELECT s FROM NotificationUserState s
        JOIN FETCH s.notification n
        WHERE s.user.id = :userId
        AND s.deleted = false
        AND n.createdAt >= :assignedAt
        ORDER BY n.createdAt DESC
        """)
    Page<NotificationUserState> findActiveByUserId(
            @Param("userId") Long userId,
            @Param("assignedAt") LocalDateTime assignedAt,
            Pageable pageable
    );

    // get the number of unread messages
    @Query("""
        SELECT COUNT(s) FROM NotificationUserState s
        JOIN s.notification n
        WHERE s.user.id = :userId
        AND s.read = false
        AND s.deleted = false
        AND n.createdAt >= :assignedAt
        """)
    long countUnread(
            @Param("userId") Long userId,
            @Param("assignedAt") LocalDateTime assignedAt
    );

    Optional<NotificationUserState> findByNotificationIdAndUserId(
            Long notificationId, Long userId
    );

    // mark all as read
    @Modifying
    @Query("""
    UPDATE NotificationUserState s SET s.read = true
    WHERE s.user.id = :userId
    AND s.deleted = false
    AND s.assignedAt <= :assignedAt
    """)
    void markAllAsRead(
            @Param("userId") Long userId,
            @Param("assignedAt") LocalDateTime assignedAt
    );

    // clear all (soft delete)
    @Modifying
    @Query("""
        UPDATE NotificationUserState s SET s.deleted = true
        WHERE s.user.id = :userId
        """)
    void deleteAllByUserId(@Param("userId") Long userId);
}