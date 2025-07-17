package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Optional<ClientDTO> create(ClientDTO dto) throws UniquenessViolationException;
    Optional<ClientDTO> update(ClientDTO newDto) throws UniquenessViolationException;
    Optional<ClientDTO> getById(Long id);
    List<ClientDTO> getAll();
    boolean delete(Long id);
}
