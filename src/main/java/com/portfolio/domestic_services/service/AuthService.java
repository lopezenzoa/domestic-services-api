package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.LoginDTO;
import com.portfolio.domestic_services.dto.RegisterDTO;

import java.util.Optional;

public interface AuthService {
    Optional<?> register(RegisterDTO dto);
    Optional<String> login(LoginDTO dto);
}
