package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {
    @Autowired private ProviderService service;

    @PostMapping("/create")
    public ResponseEntity<ProviderDTO> create(@RequestBody ProviderDTO body) {
        Optional<ProviderDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/update")
    public ResponseEntity<ProviderDTO> update(@RequestBody ProviderDTO body) {
        Optional<ProviderDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderDTO> getById(@PathVariable Long id) {
        Optional<ProviderDTO> response = service.getById(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<ProviderDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }
}
