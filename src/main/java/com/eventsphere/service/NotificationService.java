package com.eventsphere.service;

import com.eventsphere.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification send(Long eventId, Notification notification);
    List<Notification> getByEvent(Long eventId);
}
