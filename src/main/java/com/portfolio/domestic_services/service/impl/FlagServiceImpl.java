package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.dto.FlagDTO;
import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.mappers.FlagMapper;
import com.portfolio.domestic_services.model.Flag;
import com.portfolio.domestic_services.repository.FlagRepository;
import com.portfolio.domestic_services.service.ClientService;
import com.portfolio.domestic_services.service.FlagService;
import com.portfolio.domestic_services.service.ProviderService;
import com.portfolio.domestic_services.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlagServiceImpl implements FlagService {
    @Autowired private FlagRepository repository;
    @Autowired private FlagMapper mapper;
    @Autowired private ClientService clientService;
    @Autowired private ProviderService providerService;

    @Override
    public Optional<FlagDTO> create(FlagDTO dto) {
        // searching the provider
        Optional<ProviderDTO> providerOpt = providerService.getById(dto.getProvider().getId());

        // searching the client
        Optional<ClientDTO> clientOpt = clientService.getById(dto.getClient().getId());

        // checking the presence of both client and provider
        if (clientOpt.isEmpty() || providerOpt.isEmpty())
            return Optional.empty(); // btw, this is unneeded because the method of getById() throw a exception

        dto.setReportDate(LocalDateTime.now().toString());
        dto.setClient(clientOpt.get());
        dto.setProvider(providerOpt.get());

        Flag entity = mapper.toEntity(dto);
        Flag saved = repository.save(entity);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public List<FlagDTO> getByProvider(Long providerId) {
        providerService.getById(providerId); // just for throwing the ResourceNotFoundException

        List<Flag> providerFlags = repository.findAllByProviderId(providerId);
        return mapper.toDtoList(providerFlags);
    }

    @Override
    public List<FlagDTO> getByClient(Long clientId) {
        clientService.getById(clientId); // just for throwing the ResourceNotFoundException

        List<Flag> clientFlags = repository.findAllByClientId(clientId);
        return mapper.toDtoList(clientFlags);
    }

    @Override
    public List<FlagDTO> getAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("I'm sorry, but the flag with ID: " + id + " was not found");

        repository.deleteById(id);
        return true;
    }
}
