package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.CallDTO;
import com.portfolio.domestic_services.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calls")
public class CallController {
    @Autowired private CallService service;

    @PostMapping("/request")
    public ResponseEntity<CallDTO> request(@RequestBody CallDTO body) {
        Optional<CallDTO> response = service.request(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<CallDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<CallDTO>> getCallsByClient(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllByClient(id));
    }

    @GetMapping("/provider/{id}")
    public ResponseEntity<List<CallDTO>> getCallsByProvider(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllByProvider(id));
    }

    @GetMapping("/provider/{id}/requested")
    public ResponseEntity<List<CallDTO>> getRequestedCallsByProvider(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllRequestedByProvider(id));
    }

    @PutMapping("/provider/{providerId}/accept/{callId}")
    public ResponseEntity<Void> acceptCall(@PathVariable Long providerId, @PathVariable Long callId) {
        boolean accepted = service.accept(providerId, callId);

        if (accepted)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/provider/{providerId}/decline/{callId}")
    public ResponseEntity<Void> declineCall(@PathVariable Long providerId, @PathVariable Long callId) {
        boolean accepted = service.decline(providerId, callId);

        if (accepted)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }
}
