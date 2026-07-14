package com.eventsphere.service;

import com.eventsphere.entity.Ticket;

import java.util.List;

public interface TicketService {
    Ticket createTier(Long eventId, Ticket ticket);
    Ticket purchase(Long ticketTierId, Long userId);
    Ticket checkInByCode(String ticketCode);
    List<Ticket> getByEvent(Long eventId);
}
