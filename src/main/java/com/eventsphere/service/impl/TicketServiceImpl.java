package com.eventsphere.service.impl;

import com.eventsphere.entity.Event;
import com.eventsphere.entity.Ticket;
import com.eventsphere.entity.User;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.EventRepository;
import com.eventsphere.repository.TicketRepository;
import com.eventsphere.repository.UserRepository;
import com.eventsphere.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public Ticket createTier(Long eventId, Ticket ticket) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
        ticket.setEvent(event);
        ticket.setStatus(Ticket.TicketStatus.AVAILABLE);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket purchase(Long ticketTierId, Long userId) {
        Ticket tier = ticketRepository.findById(ticketTierId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket tier not found: " + ticketTierId));

        if (tier.getQuantitySold() >= tier.getQuantityAvailable()) {
            throw new IllegalArgumentException("This ticket tier is sold out");
        }

        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // In a real system, each purchase would create its own Ticket row cloned from the tier.
        // For this scaffold we mark one unit as sold and generate a unique QR-ready code.
        tier.setQuantitySold(tier.getQuantitySold() + 1);
        tier.setHolder(buyer);
        tier.setTicketCode(UUID.randomUUID().toString());
        tier.setStatus(Ticket.TicketStatus.SOLD);

        return ticketRepository.save(tier);
    }

    @Override
    public Ticket checkInByCode(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid ticket code"));

        if (ticket.getStatus() == Ticket.TicketStatus.CHECKED_IN) {
            throw new IllegalArgumentException("Ticket already checked in");
        }

        ticket.setStatus(Ticket.TicketStatus.CHECKED_IN);
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getByEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }
}
