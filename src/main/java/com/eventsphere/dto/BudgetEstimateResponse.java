package com.eventsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class BudgetEstimateResponse {
    private Double totalEstimate;
    private Map<String, Double> breakdown; // category -> amount
    private String note;
}
