package com.eventsphere.controller;

import com.eventsphere.entity.Guest;
import com.eventsphere.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @PostMapping
    public ResponseEntity<Guest> add(@PathVariable Long eventId, @RequestBody Guest guest) {
        return ResponseEntity.ok(guestService.addGuest(eventId, guest));
    }

    @GetMapping
    public ResponseEntity<List<Guest>> getAll(@PathVariable Long eventId) {
        return ResponseEntity.ok(guestService.getByEvent(eventId));
    }

    @PatchMapping("/{guestId}/rsvp")
    public ResponseEntity<Guest> rsvp(@PathVariable Long eventId, @PathVariable Long guestId,
                                       @RequestParam Guest.RsvpStatus status,
                                       @RequestParam(defaultValue = "0") int plusOnes) {
        return ResponseEntity.ok(guestService.updateRsvp(guestId, status, plusOnes));
    }

    @PatchMapping("/{guestId}/check-in")
    public ResponseEntity<Guest> checkIn(@PathVariable Long eventId, @PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.checkIn(guestId));
    }

    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> delete(@PathVariable Long eventId, @PathVariable Long guestId) {
        guestService.delete(guestId);
        return ResponseEntity.noContent().build();
    }
}
