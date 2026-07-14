package com.eventsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, length = 100)
    private String tierName; // e.g. General, VIP, Early Bird

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantityAvailable;

    @Builder.Default
    private Integer quantitySold = 0;

    @Column(unique = true, length = 64)
    private String ticketCode; // used to generate QR check-in code

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holder_id")
    private User holder;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TicketStatus status = TicketStatus.AVAILABLE;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum TicketStatus {
        AVAILABLE, SOLD, CHECKED_IN, CANCELLED
    }
}
