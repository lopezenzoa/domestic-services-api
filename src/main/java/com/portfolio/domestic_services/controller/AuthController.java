package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.LoginDTO;
import com.portfolio.domestic_services.dto.RegisterDTO;
import com.portfolio.domestic_services.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Basic operations to register and login on the system")
public class AuthController {
    @Autowired private AuthService service;

    @Operation(summary = "Register in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registered Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request for register")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO body) {
        Optional<?> response = service.register(body);

        if (response.isPresent())
            return ResponseEntity.ok(response.get());

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Login in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login Successfully (see Header 'Authorization' for jwt)"),
            @ApiResponse(responseCode = "500", description = "Bad Credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO body) {
        Optional<String> jwtOpt = service.login(body);
        return jwtOpt.map(jwt -> ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build()).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
