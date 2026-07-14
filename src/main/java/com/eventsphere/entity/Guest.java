package com.eventsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "guests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RsvpStatus rsvpStatus = RsvpStatus.PENDING;

    @Builder.Default
    private int plusOnes = 0;

    private LocalDateTime respondedAt;

    @Builder.Default
    private boolean checkedIn = false;

    private LocalDateTime checkedInAt;

    public enum RsvpStatus {
        PENDING, ACCEPTED, DECLINED, MAYBE
    }
}
