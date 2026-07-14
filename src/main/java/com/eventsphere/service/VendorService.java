package com.eventsphere.service;

import com.eventsphere.entity.Vendor;

import java.util.List;

public interface VendorService {
    Vendor create(Vendor vendor);
    Vendor update(Long id, Vendor vendor);
    void delete(Long id);
    List<Vendor> getAll();
    List<Vendor> search(String category, String city);
    List<Vendor> recommend(String eventCategory, Double maxBudget, String city);
}
