package com.eventsphere.service;

import com.eventsphere.entity.Event;

import java.util.List;

public interface EventService {
    Event create(Event event, Long organizerId);
    Event update(Long id, Event event);
    void delete(Long id);
    Event getById(Long id);
    List<Event> getAll();
    List<Event> getByOrganizer(Long organizerId);
}
