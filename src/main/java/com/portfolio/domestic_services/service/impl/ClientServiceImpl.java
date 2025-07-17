package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.mappers.ClientMapper;
import com.portfolio.domestic_services.model.Client;
import com.portfolio.domestic_services.model.Roles;
import com.portfolio.domestic_services.repository.ClientRepository;
import com.portfolio.domestic_services.service.ClientService;
import com.portfolio.domestic_services.service.UserService;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired private ClientRepository repository;
    @Autowired private ClientMapper mapper;
    @Autowired private UserService userService;

    @Override
    public Optional<ClientDTO> create(ClientDTO dto) throws UniquenessViolationException {
        userService.checkFieldsUniquenessOnCreate(dto.getEmail(), dto.getPhoneNumber(), dto.getUsername());

        dto.setRole(Roles.USER); // by default, when creating a new Client, its role is USER

        Client client = mapper.toEntity(dto);
        Client saved = repository.save(client);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public Optional<ClientDTO> update(ClientDTO newType) throws UniquenessViolationException {
        userService.checkFieldsUniquenessOnUpdate(newType.getId(), newType.getEmail(), newType.getPhoneNumber(), newType.getUsername());

        newType.setRole(Roles.USER); // by default, when updating a new Client, its role is USER

        Client updated = repository.save(mapper.toEntity(newType));
        return Optional.of(mapper.toDto(updated)); // Spring JPA manages automatically the update
    }

    @Override
    public Optional<ClientDTO> getById(Long id) {
        Optional<Client> clientOpt = repository.findById(id);
        return clientOpt.map(client -> mapper.toDto(client));
    }

    @Override
    public List<ClientDTO> getAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id))
            return false;

        repository.deleteById(id);
        return true;
    }
}
