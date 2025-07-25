package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.dto.UserDTO;
import com.portfolio.domestic_services.mappers.ClientMapper;
import com.portfolio.domestic_services.model.Client;
import com.portfolio.domestic_services.model.Roles;
import com.portfolio.domestic_services.repository.ClientRepository;
import com.portfolio.domestic_services.service.ClientService;
import com.portfolio.domestic_services.service.UserService;
import com.portfolio.domestic_services.service.exceptions.ResourceNotFoundException;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired private ClientRepository repository;
    @Autowired private ClientMapper mapper;
    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public Optional<ClientDTO> create(ClientDTO dto) throws UniquenessViolationException {
        userService.checkFieldsUniquenessOnCreate(dto.getEmail(), dto.getPhoneNumber(), dto.getUsername());

        dto.setRole(Roles.CLIENT); // by default, when creating a new Client, its role is CLIENT
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        Client client = mapper.toEntity(dto);
        Client saved = repository.save(client);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public Optional<ClientDTO> update(ClientDTO newType) throws UniquenessViolationException {
        userService.checkFieldsUniquenessOnUpdate(newType.getId(), newType.getEmail(), newType.getPhoneNumber(), newType.getUsername());

        newType.setRole(Roles.CLIENT); // by default, when updating a new Client, its role is CLIENT
        newType.setPassword(passwordEncoder.encode(newType.getPassword()));

        Client updated = repository.save(mapper.toEntity(newType));
        return Optional.of(mapper.toDto(updated)); // Spring JPA manages automatically the update
    }

    @Override
    public Optional<ClientDTO> getById(Long id) {
        Optional<Client> clientOpt = repository.findById(id);

        if (clientOpt.isEmpty())
            throw new ResourceNotFoundException("I'm sorry, but the client with ID: " + id + " was not found");

        return Optional.of(mapper.toDto(clientOpt.get()));
    }

    @Override
    public List<ClientDTO> getAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("I'm sorry, but the client with ID: " + id + " was not found");

        repository.deleteById(id);
        return true;
    }

    @Override
    public Optional<ClientDTO> getMe() {
        Optional<UserDTO> userOpt = userService.getMe();

        if (userOpt.isPresent() && userOpt.get().getRole().equals(Roles.CLIENT))
            return getById(userOpt.get().getId());

        return Optional.empty();
    }
}
