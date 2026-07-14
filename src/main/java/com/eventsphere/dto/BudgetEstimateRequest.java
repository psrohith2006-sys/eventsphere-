package com.eventsphere.dto;

import lombok.Data;

@Data
public class BudgetEstimateRequest {
    private String eventCategory;   // wedding, conference, concert...
    private Integer guestCount;
    private String city;
    private String tier;            // budget, standard, premium
}
