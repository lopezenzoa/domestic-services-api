package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.FacilityDTO;

import java.util.Optional;

public interface FacilityService {
    Optional<FacilityDTO> create(FacilityDTO dto);
    Optional<FacilityDTO> findByName(String name);
}
