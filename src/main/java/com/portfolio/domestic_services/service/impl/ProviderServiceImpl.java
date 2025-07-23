package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.mappers.ProviderMapper;
import com.portfolio.domestic_services.model.Provider;
import com.portfolio.domestic_services.model.Roles;
import com.portfolio.domestic_services.repository.ProviderRepository;
import com.portfolio.domestic_services.service.FacilityService;
import com.portfolio.domestic_services.service.ProviderService;
import com.portfolio.domestic_services.service.UserService;
import com.portfolio.domestic_services.service.exceptions.ResourceNotFoundException;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderServiceImpl implements ProviderService {
    @Autowired private ProviderRepository repository;
    @Autowired private ProviderMapper mapper;
    @Autowired private FacilityService facilityService;
    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public Optional<ProviderDTO> create(ProviderDTO dto) throws UniquenessViolationException {
        userService.checkFieldsUniquenessOnCreate(dto.getEmail(), dto.getPhoneNumber(), dto.getUsername());

        dto.setRole(Roles.PROVIDER); // by default, when creating a Provider, its role is PROVIDER
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        Provider provider = mapper.toEntity(dto);

        Optional<FacilityDTO> facilityOpt = facilityService.findByName(dto.getFacility().getName());

        // here, if the facility is found, then I have to map it to an entity in order to set it to the provider
        facilityOpt.ifPresent(facilityDto -> provider.setFacility(facilityService.mapToEntity(facilityDto)));
        Provider saved = repository.save(provider);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public Optional<ProviderDTO> update(ProviderDTO dto) throws UniquenessViolationException {
        userService.checkFieldsUniquenessOnUpdate(dto.getId(), dto.getEmail(), dto.getPhoneNumber(), dto.getUsername());

        dto.setRole(Roles.PROVIDER); // by default, when updating a Provider, its role is PROVIDER

        Provider updated = repository.save(mapper.toEntity(dto));
        return Optional.of(mapper.toDto(updated)); // Spring JPA manages automatically the update
    }

    @Override
    public Optional<ProviderDTO> getById(Long id) {
        Optional<Provider> providerOpt = repository.findById(id);

        if (providerOpt.isEmpty())
            throw new ResourceNotFoundException("I'm sorry, but the provider with ID: " + id + " was not found");

        return providerOpt.map(client -> mapper.toDto(client));
    }

    @Override
    public List<ProviderDTO> getAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("I'm sorry, but the provider with ID: " + id + " was not found");

        repository.deleteById(id);
        return true;
    }

    @Override
    public Provider mapToEntity(ProviderDTO dto) {
        return mapper.toEntity(dto);
    }
}
