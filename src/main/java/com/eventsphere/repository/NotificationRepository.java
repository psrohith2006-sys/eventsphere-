package com.eventsphere.repository;

import com.eventsphere.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEventIdOrderBySentAtDesc(Long eventId);
}
