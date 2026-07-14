package com.eventsphere.controller;

import com.eventsphere.dto.CheckInRequest;
import com.eventsphere.entity.Ticket;
import com.eventsphere.entity.User;
import com.eventsphere.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/events/{eventId}/tickets")
    public ResponseEntity<Ticket> createTier(@PathVariable Long eventId, @RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.createTier(eventId, ticket));
    }

    @GetMapping("/events/{eventId}/tickets")
    public ResponseEntity<List<Ticket>> getTiers(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getByEvent(eventId));
    }

    @PostMapping("/tickets/{ticketTierId}/purchase")
    public ResponseEntity<Ticket> purchase(@PathVariable Long ticketTierId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ticketService.purchase(ticketTierId, user.getId()));
    }

    // QR check-in: the QR code encodes the ticketCode; scanning app posts it here
    @PostMapping("/tickets/check-in")
    public ResponseEntity<Ticket> checkIn(@RequestBody CheckInRequest request) {
        return ResponseEntity.ok(ticketService.checkInByCode(request.getTicketCode()));
    }
}
