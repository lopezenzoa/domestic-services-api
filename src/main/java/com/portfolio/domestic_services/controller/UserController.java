package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.UserDTO;
import com.portfolio.domestic_services.service.UserService;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserService service;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO body) throws UniquenessViolationException {
        Optional<UserDTO> userOpt = service.create(body);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        Optional<UserDTO> userOpt = service.getById(id);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO body) throws UniquenessViolationException {
        Optional<UserDTO> userOpt = service.update(body);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }
}
