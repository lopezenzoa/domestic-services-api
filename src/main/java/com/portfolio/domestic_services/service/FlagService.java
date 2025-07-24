package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.FlagDTO;

import java.util.List;
import java.util.Optional;

public interface FlagService {
    Optional<FlagDTO> create(FlagDTO dto);
    List<FlagDTO> getByProvider(Long providerId);
    List<FlagDTO> getByClient(Long clientId);
    List<FlagDTO> getAll();
    boolean delete(Long id);

    List<FlagDTO> getMe();
}
