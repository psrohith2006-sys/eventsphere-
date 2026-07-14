package com.eventsphere.service.impl;

import com.eventsphere.entity.Event;
import com.eventsphere.entity.User;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.EventRepository;
import com.eventsphere.repository.UserRepository;
import com.eventsphere.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public Event create(Event event, Long organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found"));
        event.setOrganizer(organizer);
        return eventRepository.save(event);
    }

    @Override
    public Event update(Long id, Event updated) {
        Event existing = getById(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setStartDateTime(updated.getStartDateTime());
        existing.setEndDateTime(updated.getEndDateTime());
        existing.setVenue(updated.getVenue());
        existing.setStatus(updated.getStatus());
        existing.setTotalBudget(updated.getTotalBudget());
        return eventRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getByOrganizer(Long organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }
}
