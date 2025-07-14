package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.CallDTO;
import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.mappers.CallMapper;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.States;
import com.portfolio.domestic_services.repository.CallRepository;
import com.portfolio.domestic_services.service.CallService;
import com.portfolio.domestic_services.service.ClientService;
import com.portfolio.domestic_services.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CallServiceImpl implements CallService {
    @Autowired private CallRepository repo;
    @Autowired private CallMapper mapper;
    @Autowired private ProviderService providerService;
    @Autowired private ClientService clientService;

    @Override
    public Optional<CallDTO> request(CallDTO dto) {
        // searching the provider
        Optional<ProviderDTO> providerOpt = providerService.getById(dto.getProvider().getId());

        // searching the client
        Optional<ClientDTO> clientOpt = clientService.getById(dto.getClient().getId());

        // checking the presence of both client and provider
        if (clientOpt.isEmpty() || providerOpt.isEmpty())
            return Optional.empty();

        dto.setState(States.REQUESTING.toString());
        dto.setClient(clientOpt.get());
        dto.setProvider(providerOpt.get());

        Call call = mapper.toEntity(dto);
        Call saved = repo.save(call);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public List<CallDTO> getAll() {
        List<Call> calls = repo.findAll();
        return mapper.toDtoList(calls);
    }

    @Override
    public List<CallDTO> getAllByClient(Long id) {
        List<Call> calls = repo.findAllByClientId(id);
        return mapper.toDtoList(calls);
    }

    @Override
    public List<CallDTO> getAllByProvider(Long id) {
        List<Call> calls = repo.findAllByProviderId(id);
        return mapper.toDtoList(calls);
    }

    @Override
    public List<CallDTO> getAllRequestedByProvider(Long id) {
        List<Call> calls = repo.findAllByStateAndProviderId(States.REQUESTING, id);
        return mapper.toDtoList(calls);
    }

    @Override
    public boolean accept(Long providerId, Long callId) {
        // getting the provider's calls
        List<CallDTO> providerCalls = getAllByProvider(providerId)
                .stream()
                .filter(call -> call.getState().equals(States.REQUESTING.toString()) && call.getId().equals(callId)) // filtering all calls with state of 'REQUESTING'
                .toList();

        Optional<Call> callOpt = getById(callId);

        // this condition means: if the provider has no calls associated or the call was not found, return false
        if (providerCalls.isEmpty() || callOpt.isEmpty())
            return false;

        Call call = callOpt.get(); // this is the call before updated
        call.setState(States.PENDING); // this means that the call was accepted

        repo.save(call); // updating the call on db
        return true;
    }

    @Override
    public boolean decline(Long providerId, Long callId) {
        // getting the provider's calls
        List<CallDTO> providerCalls = getAllByProvider(providerId)
                .stream()
                .filter(call -> call.getState().equals(States.REQUESTING.toString()) && call.getId().equals(callId)) // filtering all calls with state of 'REQUESTING'
                .toList();

        // searching the call
        Optional<Call> callOpt = getById(callId);

        // this condition means: if the provider has no calls associated or the call was not found, return false
        if (providerCalls.isEmpty() || callOpt.isEmpty())
            return false;

        Call call = callOpt.get(); // this is the call before updated
        call.setState(States.DECLINED); // this means that the call was declined

        repo.save(call); // updating the call on db
        return true;
    }

    @Override
    public boolean delete(Long id) {
        if (!repo.existsById(id))
            return false;

        repo.deleteById(id);
        return true;
    }

    private Optional<Call> getById(Long id) {
        return repo.findById(id);
    }
}
