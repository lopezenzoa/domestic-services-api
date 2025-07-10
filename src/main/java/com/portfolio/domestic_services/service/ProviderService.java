package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.ProviderDTO;

import java.util.List;
import java.util.Optional;

public interface ProviderService {
    Optional<ProviderDTO> create(ProviderDTO dto);
    Optional<ProviderDTO> update(ProviderDTO newDto);
    Optional<ProviderDTO> getById(Long id);
    List<ProviderDTO> getAll();
    boolean delete(Long id);
}
