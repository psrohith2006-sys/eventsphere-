package com.eventsphere.repository;

import com.eventsphere.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByEventId(Long eventId);
}
