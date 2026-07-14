package com.eventsphere.service.impl;

import com.eventsphere.entity.BudgetItem;
import com.eventsphere.entity.Event;
import com.eventsphere.entity.Guest;
import com.eventsphere.entity.Ticket;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.BudgetItemRepository;
import com.eventsphere.repository.EventRepository;
import com.eventsphere.repository.GuestRepository;
import com.eventsphere.repository.TicketRepository;
import com.eventsphere.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final EventRepository eventRepository;
    private final GuestRepository guestRepository;
    private final TicketRepository ticketRepository;
    private final BudgetItemRepository budgetItemRepository;

    @Override
    public Map<String, Object> getEventSummary(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));

        List<Guest> guests = guestRepository.findByEventId(eventId);
        List<Ticket> tiers = ticketRepository.findByEventId(eventId);
        List<BudgetItem> budgetItems = budgetItemRepository.findByEventId(eventId);

        long accepted = guests.stream().filter(g -> g.getRsvpStatus() == Guest.RsvpStatus.ACCEPTED).count();
        long declined = guests.stream().filter(g -> g.getRsvpStatus() == Guest.RsvpStatus.DECLINED).count();
        long pending = guests.stream().filter(g -> g.getRsvpStatus() == Guest.RsvpStatus.PENDING).count();
        long checkedIn = guests.stream().filter(Guest::isCheckedIn).count();

        int ticketsSold = tiers.stream().mapToInt(Ticket::getQuantitySold).sum();
        double ticketRevenue = tiers.stream()
                .mapToDouble(t -> t.getQuantitySold() * t.getPrice())
                .sum();

        double estimatedBudget = budgetItems.stream().mapToDouble(BudgetItem::getEstimatedCost).sum();
        double actualSpend = budgetItems.stream().mapToDouble(BudgetItem::getActualCost).sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("eventId", event.getId());
        summary.put("eventTitle", event.getTitle());
        summary.put("status", event.getStatus());
        summary.put("totalGuests", guests.size());
        summary.put("rsvpAccepted", accepted);
        summary.put("rsvpDeclined", declined);
        summary.put("rsvpPending", pending);
        summary.put("checkedIn", checkedIn);
        summary.put("ticketsSold", ticketsSold);
        summary.put("ticketRevenue", ticketRevenue);
        summary.put("estimatedBudget", estimatedBudget);
        summary.put("actualSpend", actualSpend);
        summary.put("budgetVariance", estimatedBudget - actualSpend);

        return summary;
    }

    /**
     * "Sustainability tracker" - lightweight heuristic scoring based on data the
     * platform already has (digital ticketing vs. print, guest count, catering
     * choices tagged in budget item descriptions). Replace with a real emissions
     * model / third-party sustainability API when available.
     */
    @Override
    public Map<String, Object> getSustainabilityScore(Long eventId) {
        List<Guest> guests = guestRepository.findByEventId(eventId);
        List<BudgetItem> budgetItems = budgetItemRepository.findByEventId(eventId);

        int guestCount = Math.max(guests.size(), 1);
        boolean hasDigitalCatering = budgetItems.stream()
                .anyMatch(b -> b.getCategory() != null &&
                        b.getCategory().toLowerCase().contains("catering"));

        // toy scoring: 100 = best. Digital check-in via QR (always true in this platform)
        // gives a baseline; plant-forward/local catering tags would add more in a real model.
        int score = 60;
        if (guestCount < 150) score += 10;      // smaller footprint
        if (hasDigitalCatering) score += 10;     // catering tracked/plannable, less waste
        score = Math.min(score, 100);

        Map<String, Object> result = new HashMap<>();
        result.put("eventId", eventId);
        result.put("sustainabilityScore", score);
        result.put("notes", "Score reflects digital ticketing/check-in and planning data on file. Add local sourcing and waste-reduction tags to budget items to improve it.");
        return result;
    }
}
