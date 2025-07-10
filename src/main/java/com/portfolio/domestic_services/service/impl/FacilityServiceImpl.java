package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.mappers.FacilityMapper;
import com.portfolio.domestic_services.model.Facility;
import com.portfolio.domestic_services.repository.FacilityRepository;
import com.portfolio.domestic_services.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FacilityServiceImpl implements FacilityService {
    @Autowired private FacilityRepository repo;
    @Autowired private FacilityMapper mapper;

    @Override
    public Optional<FacilityDTO> create(FacilityDTO dto) {
        Facility facility = mapper.toEntity(dto);
        Facility saved = repo.save(facility);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public Optional<FacilityDTO> findByName(String name) {
        Optional<Facility> facilityOpt = repo.findByName(name);
        return facilityOpt.map(mapper::toDto);
    }
}
