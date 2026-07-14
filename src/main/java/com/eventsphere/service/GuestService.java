package com.eventsphere.service;

import com.eventsphere.entity.Guest;

import java.util.List;

public interface GuestService {
    Guest addGuest(Long eventId, Guest guest);
    Guest updateRsvp(Long guestId, Guest.RsvpStatus status, int plusOnes);
    Guest checkIn(Long guestId);
    List<Guest> getByEvent(Long eventId);
    void delete(Long guestId);
}
