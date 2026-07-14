package com.eventsphere.service.impl;

import com.eventsphere.entity.Event;
import com.eventsphere.entity.Notification;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.EventRepository;
import com.eventsphere.repository.NotificationRepository;
import com.eventsphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EventRepository eventRepository;

    @Override
    public Notification send(Long eventId, Notification notification) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
        notification.setEvent(event);
        Notification saved = notificationRepository.save(notification);
        // TODO: wire up real delivery channel (email/SMS/push/websocket) here.
        // Stubbed for now: persisted rows are polled by the frontend for "live" announcements.
        return saved;
    }

    @Override
    public List<Notification> getByEvent(Long eventId) {
        return notificationRepository.findByEventIdOrderBySentAtDesc(eventId);
    }
}
