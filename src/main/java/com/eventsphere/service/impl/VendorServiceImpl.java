package com.eventsphere.service.impl;

import com.eventsphere.entity.Vendor;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.VendorRepository;
import com.eventsphere.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public Vendor create(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @Override
    public Vendor update(Long id, Vendor updated) {
        Vendor existing = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + id));
        existing.setBusinessName(updated.getBusinessName());
        existing.setCategory(updated.getCategory());
        existing.setCity(updated.getCity());
        existing.setDescription(updated.getDescription());
        existing.setContactEmail(updated.getContactEmail());
        existing.setContactPhone(updated.getContactPhone());
        existing.setPriceRangeMin(updated.getPriceRangeMin());
        existing.setPriceRangeMax(updated.getPriceRangeMax());
        return vendorRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        vendorRepository.deleteById(id);
    }

    @Override
    public List<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    @Override
    public List<Vendor> search(String category, String city) {
        if (category != null && !category.isBlank()) {
            return vendorRepository.findByCategoryIgnoreCase(category);
        }
        if (city != null && !city.isBlank()) {
            return vendorRepository.findByCityIgnoreCase(city);
        }
        return vendorRepository.findAll();
    }

    /**
     * "Smart vendor recommendations" - scores vendors by category match, city match,
     * and how well their price range fits the given budget, then returns the
     * best-fitting vendors sorted by score. A transparent, explainable heuristic;
     * can be swapped for a learned ranking model later.
     */
    @Override
    public List<Vendor> recommend(String eventCategory, Double maxBudget, String city) {
        return vendorRepository.findAll().stream()
                .filter(v -> eventCategory == null || eventCategory.isBlank()
                        || v.getCategory().equalsIgnoreCase(eventCategory))
                .sorted(Comparator.comparingDouble(
                        (Vendor v) -> -score(v, maxBudget, city)))
                .collect(Collectors.toList());
    }

    private double score(Vendor v, Double maxBudget, String city) {
        double score = v.getRating() == null ? 0 : v.getRating();

        if (city != null && !city.isBlank() && v.getCity() != null
                && v.getCity().equalsIgnoreCase(city)) {
            score += 2.0;
        }

        if (maxBudget != null && v.getPriceRangeMin() != null && v.getPriceRangeMin() <= maxBudget) {
            score += 1.5;
        }

        if (v.isVerified()) {
            score += 1.0;
        }

        return score;
    }
}
