package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.CallDTO;
import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.dto.ShiftDTO;
import com.portfolio.domestic_services.mappers.CallMapper;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.States;
import com.portfolio.domestic_services.repository.CallRepository;
import com.portfolio.domestic_services.service.CallService;
import com.portfolio.domestic_services.service.ClientService;
import com.portfolio.domestic_services.service.ProviderService;
import com.portfolio.domestic_services.service.ShiftService;
import com.portfolio.domestic_services.service.exceptions.EmptyCollectionException;
import com.portfolio.domestic_services.service.exceptions.ResourceNotFoundException;
import com.portfolio.domestic_services.service.exceptions.DateNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CallServiceImpl implements CallService {
    @Autowired private CallRepository repository;
    @Autowired private CallMapper mapper;
    @Autowired private ProviderService providerService;
    @Autowired private ClientService clientService;
    @Autowired private ShiftService shiftService;

    @Override
    public Optional<CallDTO> request(CallDTO dto) {
        // searching the provider
        Optional<ProviderDTO> providerOpt = providerService.getById(dto.getProvider().getId());

        // searching the client
        Optional<ClientDTO> clientOpt = clientService.getById(dto.getClient().getId());

        // checking the presence of both client and provider
        if (clientOpt.isEmpty() || providerOpt.isEmpty())
            return Optional.empty(); // btw, this is unneeded because the method of getById() throw a exception

        // checking that the requested dto is an available Shift of the Provider
        boolean isShiftAvailable = shiftService.checkRequestedDate(dto.getDate(), providerOpt.get().getId());

        if (!isShiftAvailable)
            throw new DateNotAllowedException("I'm sorry but the date you requested doesn't correspond to an available Shift of the Provider");

        dto.setState(States.REQUESTING.toString());
        dto.setClient(clientOpt.get());
        dto.setProvider(providerOpt.get());

        Call call = mapper.toEntity(dto);
        Call saved = repository.save(call);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public List<CallDTO> getAll() {
        List<Call> calls = repository.findAll();
        return mapper.toDtoList(calls);
    }

    @Override
    public List<CallDTO> getAllByClient(Long id) {
        clientService.getById(id); // just checking if the id given is correct

        List<Call> calls = repository.findAllByClientId(id);
        return mapper.toDtoList(calls);
    }

    @Override
    public List<CallDTO> getAllByProvider(Long id) {
        providerService.getById(id); // just checking if the id given is correct

        List<Call> calls = repository.findAllByProviderId(id);
        return mapper.toDtoList(calls);
    }

    @Override
    public List<CallDTO> getAllRequestedByProvider(Long id) {
        providerService.getById(id); // just checking if the id given is correct

        List<Call> calls = repository.findAllByStateAndProviderId(States.REQUESTING, id);
        return mapper.toDtoList(calls);
    }

    @Override
    public boolean accept(Long providerId, Long callId) {
        List<CallDTO> providerCalls = getAllRequestedByProvider(providerId);

        Optional<Call> callOpt = getById(callId);

        // this condition means: if the provider has no calls associated or the call was not found, return false
        if (providerCalls.isEmpty() || callOpt.isEmpty())
            throw new EmptyCollectionException("The provider hasn't any requested call to accept");

        // updating the Shift of the Provider to do it unavailable for other calls
        shiftService.takeShiftOfProvider(providerId, callOpt.get().getDate());

        Call call = callOpt.get(); // this is the call before updated
        call.setState(States.PENDING); // this means that the call was accepted

        repository.save(call); // updating the call on db
        return true;
    }

    @Override
    public boolean decline(Long providerId, Long callId) {
        List<CallDTO> providerCalls = getAllRequestedByProvider(providerId);

        // searching the call
        Optional<Call> callOpt = getById(callId);

        // this condition means: if the provider has no calls associated or the call was not found, return false
        if (providerCalls.isEmpty() || callOpt.isEmpty())
            throw new EmptyCollectionException("The provider hasn't any requested call to decline");

        Call call = callOpt.get(); // this is the call before updated
        call.setState(States.DECLINED); // this means that the call was declined

        repository.save(call); // updating the call on db
        return true;
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("I'm sorry, but the call with ID: " + id + " was not found");

        repository.deleteById(id);
        return true;
    }

    private Optional<Call> getById(Long id) {
        Optional<Call> callOpt = repository.findById(id);

        if (callOpt.isEmpty())
            throw new ResourceNotFoundException("I'm sorry, but the call with ID: " + id + " was not found");

        return callOpt;
    }
}
