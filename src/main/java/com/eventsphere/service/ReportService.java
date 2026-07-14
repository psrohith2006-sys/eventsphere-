package com.eventsphere.service;

import java.util.Map;

public interface ReportService {
    Map<String, Object> getEventSummary(Long eventId);
    Map<String, Object> getSustainabilityScore(Long eventId);
}
