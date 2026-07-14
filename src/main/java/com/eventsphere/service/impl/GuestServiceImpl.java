package com.eventsphere.service.impl;

import com.eventsphere.entity.Event;
import com.eventsphere.entity.Guest;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.EventRepository;
import com.eventsphere.repository.GuestRepository;
import com.eventsphere.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final EventRepository eventRepository;

    @Override
    public Guest addGuest(Long eventId, Guest guest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
        guest.setEvent(event);
        return guestRepository.save(guest);
    }

    @Override
    public Guest updateRsvp(Long guestId, Guest.RsvpStatus status, int plusOnes) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found: " + guestId));
        guest.setRsvpStatus(status);
        guest.setPlusOnes(plusOnes);
        guest.setRespondedAt(LocalDateTime.now());
        return guestRepository.save(guest);
    }

    @Override
    public Guest checkIn(Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found: " + guestId));
        guest.setCheckedIn(true);
        guest.setCheckedInAt(LocalDateTime.now());
        return guestRepository.save(guest);
    }

    @Override
    public List<Guest> getByEvent(Long eventId) {
        return guestRepository.findByEventId(eventId);
    }

    @Override
    public void delete(Long guestId) {
        guestRepository.deleteById(guestId);
    }
}
