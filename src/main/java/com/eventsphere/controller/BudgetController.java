package com.eventsphere.controller;

import com.eventsphere.dto.BudgetEstimateRequest;
import com.eventsphere.dto.BudgetEstimateResponse;
import com.eventsphere.entity.BudgetItem;
import com.eventsphere.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/events/{eventId}/budget-items")
    public ResponseEntity<BudgetItem> add(@PathVariable Long eventId, @RequestBody BudgetItem item) {
        return ResponseEntity.ok(budgetService.addItem(eventId, item));
    }

    @GetMapping("/events/{eventId}/budget-items")
    public ResponseEntity<List<BudgetItem>> getAll(@PathVariable Long eventId) {
        return ResponseEntity.ok(budgetService.getByEvent(eventId));
    }

    @PutMapping("/budget-items/{itemId}")
    public ResponseEntity<BudgetItem> update(@PathVariable Long itemId, @RequestBody BudgetItem item) {
        return ResponseEntity.ok(budgetService.updateItem(itemId, item));
    }

    @DeleteMapping("/budget-items/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Long itemId) {
        budgetService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }

    // "AI budget estimator" endpoint - see BudgetServiceImpl for the heuristic used
    @PostMapping("/budget/estimate")
    public ResponseEntity<BudgetEstimateResponse> estimate(@RequestBody BudgetEstimateRequest request) {
        return ResponseEntity.ok(budgetService.estimate(request));
    }
}
