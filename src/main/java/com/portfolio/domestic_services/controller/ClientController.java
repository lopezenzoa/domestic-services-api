package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService service;

    @PostMapping("/create")
    public ResponseEntity<ClientDTO> create(@RequestBody ClientDTO body) {
        Optional<ClientDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/update")
    public ResponseEntity<ClientDTO> update(@RequestBody ClientDTO body) {
        Optional<ClientDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getById(@PathVariable Long id) {
        Optional<ClientDTO> response = service.getById(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> getAll() {
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
