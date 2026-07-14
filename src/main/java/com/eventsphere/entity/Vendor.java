package com.eventsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String businessName;

    @Column(nullable = false, length = 100)
    private String category; // catering, photography, decor, music, venue...

    @Column(length = 150)
    private String city;

    @Column(length = 2000)
    private String description;

    @Column(length = 150)
    private String contactEmail;

    @Column(length = 20)
    private String contactPhone;

    @Builder.Default
    private Double priceRangeMin = 0.0;

    @Builder.Default
    private Double priceRangeMax = 0.0;

    @Builder.Default
    private Double rating = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner; // linked user account with role VENDOR

    @Builder.Default
    private boolean verified = false;
}
