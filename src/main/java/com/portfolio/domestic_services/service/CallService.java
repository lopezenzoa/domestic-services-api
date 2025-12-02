package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.CallDTO;

import java.util.List;
import java.util.Optional;

import com.portfolio.domestic_services.dto.ChatListDTO;
import com.portfolio.domestic_services.model.Call;
import org.springframework.data.domain.Page;
public interface CallService {
    Optional<CallDTO> request(CallDTO dto);
    List<CallDTO> getAll();
    Optional<CallDTO> getProviderCallDetail(Long providerId, Long callId);
    boolean finish(Long providerId, Long callId);
    List<CallDTO> getHistoryForProvider(Long providerId, String state, String start, String end);
    Call findById(Long id);

    List<CallDTO> getAllByClient(Long clientId);
    List<CallDTO> getAllByProvider(Long providerId);
    List<CallDTO> getAllRequestedByProvider(Long providerId);
    boolean accept(Long providerId, Long callId);
    boolean decline(Long providerId, Long callId);
    boolean delete(Long id);
    Page<CallDTO> getPaginatedByProvider(Long providerId, int page, int size);
    Page<CallDTO> getProviderHistory(Long providerId, int page, int size);
    List<ChatListDTO> getMyChats();

    List<CallDTO> getMe();
}
