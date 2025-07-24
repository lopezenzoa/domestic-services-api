package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.CallDTO;

import java.util.List;
import java.util.Optional;

public interface CallService {
    Optional<CallDTO> request(CallDTO dto);
    List<CallDTO> getAll();

    List<CallDTO> getAllByClient(Long clientId);
    List<CallDTO> getAllByProvider(Long providerId);
    List<CallDTO> getAllRequestedByProvider(Long providerId);
    boolean accept(Long providerId, Long callId);
    boolean decline(Long providerId, Long callId);
    boolean delete(Long id);

    List<CallDTO> getMe();
}
