package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.ReviewDTO;
import com.portfolio.domestic_services.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired private ReviewService service;

    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewDTO body) {
        Optional<ReviewDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<ReviewDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllByClient(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllByClient(id));
    }

    @GetMapping("/provider/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllByProvider(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllByProvider(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }
}
