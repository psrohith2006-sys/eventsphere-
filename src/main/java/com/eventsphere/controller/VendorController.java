package com.eventsphere.controller;

import com.eventsphere.entity.Vendor;
import com.eventsphere.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<Vendor> create(@RequestBody Vendor vendor) {
        return ResponseEntity.ok(vendorService.create(vendor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> update(@PathVariable Long id, @RequestBody Vendor vendor) {
        return ResponseEntity.ok(vendorService.update(id, vendor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vendorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> search(@RequestParam(required = false) String category,
                                                @RequestParam(required = false) String city) {
        return ResponseEntity.ok(vendorService.search(category, city));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Vendor>> recommend(@RequestParam(required = false) String eventCategory,
                                                   @RequestParam(required = false) Double maxBudget,
                                                   @RequestParam(required = false) String city) {
        return ResponseEntity.ok(vendorService.recommend(eventCategory, maxBudget, city));
    }
}
