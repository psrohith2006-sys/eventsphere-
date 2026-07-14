package com.eventsphere.controller;

import com.eventsphere.entity.Notification;
import com.eventsphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> send(@PathVariable Long eventId, @RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.send(eventId, notification));
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAll(@PathVariable Long eventId) {
        return ResponseEntity.ok(notificationService.getByEvent(eventId));
    }
}
