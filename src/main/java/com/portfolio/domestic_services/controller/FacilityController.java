package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {
    @Autowired private FacilityService service;

    @PostMapping("/create")
    public ResponseEntity<FacilityDTO> create(@RequestBody FacilityDTO body) {
        Optional<FacilityDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/search")
    public ResponseEntity<FacilityDTO> findByName(@RequestParam String name) {
        Optional<FacilityDTO> facilityOpt = service.findByName(name);
        return facilityOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
