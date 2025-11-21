package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.model.Facility;

import java.util.List;
import java.util.Optional;

public interface FacilityService {
    Optional<FacilityDTO> create(FacilityDTO dto);
    List<FacilityDTO> getAll(String query);
    Optional<FacilityDTO> findByName(String name);
    Optional<FacilityDTO> getById(Long id);
    Optional<FacilityDTO> update(FacilityDTO dto);
    boolean delete(Long id);
    Facility mapToEntity(FacilityDTO dto);
}
