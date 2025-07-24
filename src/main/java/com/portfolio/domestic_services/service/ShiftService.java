package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.ShiftDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftService {
    Optional<ShiftDTO> create(ShiftDTO dto, Long providerId);
    Optional<ShiftDTO> update(ShiftDTO newDto, Long providerId);
    List<ShiftDTO> getAllByProviderId(Long providerId);
    List<ShiftDTO> getAvailableByProviderId(Long providerId);
    boolean delete(Long id, Long providerId);

    // methods related to the calls
    boolean checkRequestedDate(String dtoDate, Long providerId);
    void takeShiftOfProvider(Long providerId, LocalDateTime callDate);

    List<ShiftDTO> getMe();
}
