package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.FlagDTO;
import com.portfolio.domestic_services.service.FlagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flags")
public class FlagController {
    @Autowired private FlagService service;

    @PostMapping("/create")
    public ResponseEntity<FlagDTO> create(@RequestBody FlagDTO body) {
        Optional<FlagDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<FlagDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<FlagDTO>> getByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(service.getByProvider(providerId));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<FlagDTO>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(service.getByClient(clientId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }
}
