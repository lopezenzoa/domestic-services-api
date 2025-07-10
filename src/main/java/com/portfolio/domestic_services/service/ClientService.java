package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.RegisterDataDTO;
import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.model.Provider;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Provider buildProvider(RegisterDataDTO dto);
    Optional<ClientDTO> create(ClientDTO dto);
    Optional<ClientDTO> update(ClientDTO newDto);
    Optional<ClientDTO> getById(Long id);
    List<ClientDTO> getAll();
    boolean delete(Long id);
}
