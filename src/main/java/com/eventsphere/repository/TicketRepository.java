package com.eventsphere.repository;

import com.eventsphere.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEventId(Long eventId);
    Optional<Ticket> findByTicketCode(String ticketCode);
}
