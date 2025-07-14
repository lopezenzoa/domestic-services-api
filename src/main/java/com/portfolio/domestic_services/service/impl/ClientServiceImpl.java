package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.mappers.ClientMapper;
import com.portfolio.domestic_services.model.Client;
import com.portfolio.domestic_services.repository.ClientRepository;
import com.portfolio.domestic_services.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired private ClientRepository repo;
    @Autowired private ClientMapper mapper;

    @Override
    public Optional<ClientDTO> create(ClientDTO dto) {
        Client client = mapper.toEntity(dto);
        Client saved = repo.save(client);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public Optional<ClientDTO> update(ClientDTO newType) {
        Client updated = repo.save(mapper.toEntity(newType));
        return Optional.of(mapper.toDto(updated)); // Spring JPA manages automatically the update
    }

    @Override
    public Optional<ClientDTO> getById(Long id) {
        Optional<Client> clientOpt = repo.findById(id);
        return clientOpt.map(client -> mapper.toDto(client));
    }

    @Override
    public List<ClientDTO> getAll() {
        return mapper.toDtoList(repo.findAll());
    }

    @Override
    public boolean delete(Long id) {
        if (!repo.existsById(id))
            return false;

        repo.deleteById(id);
        return true;
    }
}
