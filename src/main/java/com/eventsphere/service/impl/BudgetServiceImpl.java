package com.eventsphere.service.impl;

import com.eventsphere.dto.BudgetEstimateRequest;
import com.eventsphere.dto.BudgetEstimateResponse;
import com.eventsphere.entity.BudgetItem;
import com.eventsphere.entity.Event;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.BudgetItemRepository;
import com.eventsphere.repository.EventRepository;
import com.eventsphere.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * BudgetServiceImpl includes the "AI budget estimator" feature.
 * NOTE: this is implemented as a transparent, rule-based heuristic (per-guest cost
 * multipliers by category/tier), not a trained ML model. It is a placeholder that
 * produces a reasonable first-pass estimate and a category breakdown; swap the
 * calculateBreakdown() method for a real ML/LLM-backed service later without
 * changing the controller or DTO contract.
 */
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetItemRepository budgetItemRepository;
    private final EventRepository eventRepository;

    // base cost per guest, per category, in a generic currency unit
    private static final Map<String, Double> BASE_PER_GUEST = Map.of(
            "venue", 25.0,
            "catering", 40.0,
            "decor", 10.0,
            "entertainment", 8.0,
            "photography", 6.0,
            "staffing", 7.0,
            "miscellaneous", 5.0
    );

    private static final Map<String, Double> CATEGORY_MULTIPLIER = Map.of(
            "wedding", 1.4,
            "conference", 1.1,
            "concert", 1.3,
            "corporate", 1.0,
            "birthday", 0.8,
            "other", 1.0
    );

    private static final Map<String, Double> TIER_MULTIPLIER = Map.of(
            "budget", 0.7,
            "standard", 1.0,
            "premium", 1.6
    );

    @Override
    public BudgetItem addItem(Long eventId, BudgetItem item) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
        item.setEvent(event);
        return budgetItemRepository.save(item);
    }

    @Override
    public BudgetItem updateItem(Long itemId, BudgetItem updated) {
        BudgetItem existing = budgetItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget item not found: " + itemId));
        existing.setCategory(updated.getCategory());
        existing.setDescription(updated.getDescription());
        existing.setEstimatedCost(updated.getEstimatedCost());
        existing.setActualCost(updated.getActualCost());
        existing.setPaid(updated.isPaid());
        return budgetItemRepository.save(existing);
    }

    @Override
    public void deleteItem(Long itemId) {
        budgetItemRepository.deleteById(itemId);
    }

    @Override
    public List<BudgetItem> getByEvent(Long eventId) {
        return budgetItemRepository.findByEventId(eventId);
    }

    @Override
    public BudgetEstimateResponse estimate(BudgetEstimateRequest request) {
        int guests = request.getGuestCount() == null ? 50 : Math.max(request.getGuestCount(), 1);
        double categoryMult = CATEGORY_MULTIPLIER.getOrDefault(
                safeLower(request.getEventCategory()), 1.0);
        double tierMult = TIER_MULTIPLIER.getOrDefault(safeLower(request.getTier()), 1.0);

        Map<String, Double> breakdown = new LinkedHashMap<>();
        double total = 0.0;
        for (Map.Entry<String, Double> entry : BASE_PER_GUEST.entrySet()) {
            double amount = entry.getValue() * guests * categoryMult * tierMult;
            amount = Math.round(amount * 100.0) / 100.0;
            breakdown.put(entry.getKey(), amount);
            total += amount;
        }

        total = Math.round(total * 100.0) / 100.0;

        return BudgetEstimateResponse.builder()
                .totalEstimate(total)
                .breakdown(breakdown)
                .note("Heuristic estimate based on guest count, event type, and tier. Refine with real vendor quotes as they come in.")
                .build();
    }

    private String safeLower(String s) {
        return s == null ? "" : s.toLowerCase();
    }
}
