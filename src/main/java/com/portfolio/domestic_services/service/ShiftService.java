package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.ShiftDTO;

import java.util.List;
import java.util.Optional;

public interface ShiftService {
    Optional<ShiftDTO> create(ShiftDTO dto, Long providerId);
    Optional<ShiftDTO> update(ShiftDTO newDto, Long providerId);
    List<ShiftDTO> getAllByProviderId(Long providerId);
    List<ShiftDTO> getAvailableByProviderId(Long providerId);
    boolean delete(Long id, Long providerId);
}
