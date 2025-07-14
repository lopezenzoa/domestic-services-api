package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.ClientDTO;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Optional<ClientDTO> create(ClientDTO dto);
    Optional<ClientDTO> update(ClientDTO newDto);
    Optional<ClientDTO> getById(Long id);
    List<ClientDTO> getAll();
    boolean delete(Long id);
}
