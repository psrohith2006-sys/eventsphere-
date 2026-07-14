package com.eventsphere.controller;

import com.eventsphere.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/events/{eventId}/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> summary(@PathVariable Long eventId) {
        return ResponseEntity.ok(reportService.getEventSummary(eventId));
    }

    @GetMapping("/sustainability")
    public ResponseEntity<Map<String, Object>> sustainability(@PathVariable Long eventId) {
        return ResponseEntity.ok(reportService.getSustainabilityScore(eventId));
    }
}
