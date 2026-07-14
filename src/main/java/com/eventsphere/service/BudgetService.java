package com.eventsphere.service;

import com.eventsphere.dto.BudgetEstimateRequest;
import com.eventsphere.dto.BudgetEstimateResponse;
import com.eventsphere.entity.BudgetItem;

import java.util.List;

public interface BudgetService {
    BudgetItem addItem(Long eventId, BudgetItem item);
    BudgetItem updateItem(Long itemId, BudgetItem item);
    void deleteItem(Long itemId);
    List<BudgetItem> getByEvent(Long eventId);
    BudgetEstimateResponse estimate(BudgetEstimateRequest request);
}
