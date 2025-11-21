package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.dto.UserDTO;
import com.portfolio.domestic_services.mappers.FacilityMapper;
import com.portfolio.domestic_services.model.Facility;
import com.portfolio.domestic_services.model.User;
import com.portfolio.domestic_services.repository.FacilityRepository;
import com.portfolio.domestic_services.service.FacilityService;
import com.portfolio.domestic_services.service.exceptions.ResourceNotFoundException;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacilityServiceImpl implements FacilityService {
    @Autowired private FacilityRepository repository;
    @Autowired private FacilityMapper mapper;

    @Override
    public Optional<FacilityDTO> create(FacilityDTO dto) {
        checkUniquenessOnCreate(dto.getName());

        Facility facility = mapper.toEntity(dto);
        Facility saved = repository.save(facility);

        return Optional.of(mapper.toDto(saved));
    }

    // ❗ CORRECCIÓN: Método getAll con filtro implementado ❗
    @Override
    public List<FacilityDTO> getAll(String query) {
        List<FacilityDTO> facilities = mapper.toDtoList(repository.findAll());

        if (query != null && !query.trim().isEmpty()) {
            final String lowerCaseQuery = query.toLowerCase().trim();

            // Filtramos la lista de DTOs por nombre o descripción que contenga el query
            return facilities.stream()
                    .filter(facility ->
                            facility.getName().toLowerCase().contains(lowerCaseQuery) ||
                                    (facility.getDescription() != null && facility.getDescription().toLowerCase().contains(lowerCaseQuery))
                    )
                    .collect(Collectors.toList());
        }

        // Si no hay query, devuelve la lista completa
        return facilities;
    }

    @Override
    public Optional<FacilityDTO> findByName(String name) {
        Optional<Facility> facilityOpt = repository.findByName(name);

        if (facilityOpt.isEmpty())
            throw new ResourceNotFoundException("I'm sorry but the facility with name: " + name + " was not found");

        return facilityOpt.map(mapper::toDto);
    }

    @Override
    public Optional<FacilityDTO> getById(Long id) {
        Optional<Facility> facilityOpt = repository.findById(id);

        if (facilityOpt.isEmpty())
            throw new ResourceNotFoundException("I'm sorry, but the facility with ID: " + id + " was not found");

        return Optional.of(mapper.toDto(facilityOpt.get()));
    }

    @Override
    public Optional<FacilityDTO> update(FacilityDTO dto) throws UniquenessViolationException {
        checkUniquenessOnUpdate(dto.getId(), dto.getName());

        Facility entity = mapper.toEntity(dto);
        Facility saved = repository.save(entity);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public Facility mapToEntity(FacilityDTO dto) {
        return mapper.toEntity(dto);
    }

    private void checkUniquenessOnCreate(String name) {
        boolean exists = repository.existsByName(name);

        if (exists)
            throw new UniquenessViolationException("I'm sorry but there's already another facility with the name: " + name);
    }

    private void checkUniquenessOnUpdate(Long id, String name) {
        // Usamos getAll(null) para obtener la lista completa sin filtro
        List<FacilityDTO> filteredFacilities = getAll(null)
                .stream()
                .filter(facility -> !facility.getId().equals(id))
                .toList();

        boolean exists = filteredFacilities
                .stream()
                .anyMatch(facility -> facility.getName().equalsIgnoreCase(name));

        if (exists)
            throw new UniquenessViolationException("I'm sorry but there's already another facility with the name: " + name);
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("I'm sorry, but the facility with ID: " + id + " was not found");

        repository.deleteById(id);
        return true;
    }
}