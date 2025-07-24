package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.model.Provider;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;

import java.util.List;
import java.util.Optional;

public interface ProviderService {
    Optional<ProviderDTO> create(ProviderDTO dto) throws UniquenessViolationException;
    Optional<ProviderDTO> update(ProviderDTO newDto) throws UniquenessViolationException;
    Optional<ProviderDTO> getById(Long id);
    List<ProviderDTO> getAll();
    boolean delete(Long id);
    Provider mapToEntity(ProviderDTO dto);

    Optional<ProviderDTO> getMe();
}
