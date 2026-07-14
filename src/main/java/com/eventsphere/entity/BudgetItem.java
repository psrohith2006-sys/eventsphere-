package com.eventsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "budget_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, length = 120)
    private String category; // venue, catering, decor, entertainment, staff...

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Double estimatedCost;

    @Builder.Default
    private Double actualCost = 0.0;

    @Builder.Default
    private boolean paid = false;
}
