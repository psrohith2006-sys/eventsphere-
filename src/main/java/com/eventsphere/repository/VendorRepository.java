package com.eventsphere.repository;

import com.eventsphere.entity.Vendor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Override
    @EntityGraph(attributePaths = "owner")
    List<Vendor> findAll();

    @EntityGraph(attributePaths = "owner")
    List<Vendor> findByCategoryIgnoreCase(String category);

    @EntityGraph(attributePaths = "owner")
    List<Vendor> findByCityIgnoreCase(String city);
}