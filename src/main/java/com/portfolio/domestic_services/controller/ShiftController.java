package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.ShiftDTO;
import com.portfolio.domestic_services.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/providers/shifts")
public class ShiftController {
    @Autowired private ShiftService service;

    // I have to think a way of build URLs in a cleaner format, since this is show as follows: /api/providers/shifts/{6, 7, 8, ...}/create
    @PostMapping("/{providerId}/create")
    public ResponseEntity<ShiftDTO> create(@RequestBody ShiftDTO body, @PathVariable Long providerId) {
        Optional<ShiftDTO> response = service.create(body, providerId);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{providerId}")
    public ResponseEntity<List<ShiftDTO>> getAllByProviderId(@PathVariable Long providerId) {
        return ResponseEntity.ok(service.getAllByProviderId(providerId));
    }

    @GetMapping("/{providerId}/available")
    public ResponseEntity<List<ShiftDTO>> getAvailableByProviderId(@PathVariable Long providerId) {
        return ResponseEntity.ok(service.getAvailableByProviderId(providerId));
    }

    @PutMapping("/{providerId}/update")
    public ResponseEntity<ShiftDTO> update(@RequestBody ShiftDTO body, @PathVariable Long providerId) {
        Optional<ShiftDTO> response = service.update(body, providerId);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{providerId}/delete/{shiftId}")
    public ResponseEntity<Void> delete(@PathVariable Long providerId, @PathVariable Long shiftId) {
        boolean deleted = service.delete(shiftId, providerId);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }
}
