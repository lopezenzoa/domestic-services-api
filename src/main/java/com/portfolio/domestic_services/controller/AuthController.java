package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.LoginDTO;
import com.portfolio.domestic_services.dto.RegisterDTO;
import com.portfolio.domestic_services.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO body) {
        Optional<?> response = service.register(body);

        if (response.isPresent())
            return ResponseEntity.ok(response.get());

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO body) {
        Optional<String> jwtOpt = service.login(body);
        return jwtOpt.map(jwt -> ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build()).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
