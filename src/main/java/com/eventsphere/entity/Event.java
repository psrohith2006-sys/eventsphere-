package com.eventsphere.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(length = 100)
    private String category; // wedding, conference, concert, corporate...

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Column(length = 200)
    private String venue;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventStatus status = EventStatus.PLANNING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User organizer;

    @Builder.Default
    private Double totalBudget = 0.0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum EventStatus {
        PLANNING, PUBLISHED, ONGOING, COMPLETED, CANCELLED
    }
}
