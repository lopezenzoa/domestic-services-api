package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.*;
import com.portfolio.domestic_services.mappers.CallMapper;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.Roles;
import com.portfolio.domestic_services.model.States;
import com.portfolio.domestic_services.repository.CallRepository;
import com.portfolio.domestic_services.service.*;
import com.portfolio.domestic_services.service.exceptions.EmptyCollectionException;
import com.portfolio.domestic_services.service.exceptions.ResourceNotFoundException;
import com.portfolio.domestic_services.service.exceptions.DateNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Autowired private UserService userService;

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
    public Page<CallDTO> getPaginatedByProvider(Long id, int page, int size) {

        providerService.getById(id); // validar existencia

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        Page<Call> calls = repository.findAllByProviderId(id, pageable);

        return calls.map(mapper::toDto);
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
    public List<CallDTO> getHistoryForProvider(Long providerId, String state, String start, String end) {

        List<CallDTO> all = mapper.toDtoList(repository.findAllByProviderId(providerId));

        // FILTRO POR ESTADO
        if (state != null && !state.isEmpty()) {
            all = all.stream()
                    .filter(c -> c.getState().equalsIgnoreCase(state))
                    .toList();
        }

        // FILTRO POR FECHAS (correcto)
        if (start != null && !start.isEmpty() && end != null && !end.isEmpty()) {

            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate   = LocalDate.parse(end);

            LocalDateTime desde = startDate.atStartOfDay();          // 00:00
            LocalDateTime hasta = endDate.atTime(23, 59, 59);        // 23:59:59

            all = all.stream()
                    .filter(c -> {
                        LocalDateTime fecha = LocalDateTime.parse(c.getDate());
                        return (!fecha.isBefore(desde) && !fecha.isAfter(hasta));
                    })
                    .toList();
        }

        return all;
    }


    @Override
    public boolean accept(Long providerId, Long callId) {

        log.info("=== ACEPTANDO VISITA ===");
        log.info("ProviderId recibido: " + providerId);
        log.info("CallId recibido: " + callId);

        Call call = getById(callId)
                .orElseThrow(() -> new ResourceNotFoundException("Call not found"));

        log.info("Call encontrada pertenece al provider: " + call.getProvider().getId());
        log.info("Estado actual de la visita: " + call.getState());

        // Validar que la visita pertenece al prestador logueado
        if (!call.getProvider().getId().equals(providerId)) {
            log.error("ERROR: La visita NO pertenece a este provider");
            throw new IllegalStateException("This call does not belong to this provider");
        }

        // Solo se aceptan visitas REQUESTING
        if (!call.getState().equals(States.REQUESTING)) {
            log.error("ERROR: No se puede aceptar porque el estado no es REQUESTING");
            throw new IllegalStateException("Only REQUESTING calls can be accepted");
        }

        // Tomar el turno
        log.info("Turno válido, tomando shift...");
        shiftService.takeShiftOfProvider(providerId, call.getDate());

        // Cambiar estado
        call.setState(States.PENDING);
        repository.save(call);

        log.info("=== VISITA ACEPTADA CORRECTAMENTE ===");
        return true;
    }
    @Override
    public Optional<CallDTO> getProviderCallDetail(Long providerId, Long callId) {

        Call call = repository.findById(callId)
                .orElseThrow(() -> new ResourceNotFoundException("Call not found"));

        // Validar que la visita pertenezca a ese provider
        if (!call.getProvider().getId().equals(providerId))
            throw new IllegalStateException("This call does not belong to this provider");

        // Retornamos TODA la info ampliada (DTO ya contiene cliente, costo, review, etc.)
        return Optional.of(mapper.toDto(call));
    }
    public boolean finish(Long providerId, Long callId) {
        Optional<Call> callOpt = repository.findById(callId);

        if (callOpt.isEmpty()) return false;

        Call call = callOpt.get();

        // Solo puede finalizarlo el prestador dueño
        if (!call.getProvider().getId().equals(providerId)) return false;

        call.setState(States.FINISHED);
        repository.save(call);

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

    @Override
    public List<CallDTO> getMe() {
        Optional<UserDTO> userOpt = userService.getMe();

        if (userOpt.isPresent() && userOpt.get().getRole().equals(Roles.CLIENT))
            return getAllByClient(userOpt.get().getId());

        if (userOpt.isPresent() && userOpt.get().getRole().equals(Roles.PROVIDER))
            return getAllByProvider(userOpt.get().getId());

        throw new EmptyCollectionException("I'm sorry, you don't requested any call");
    }

    private Optional<Call> getById(Long id) {
        Optional<Call> callOpt = repository.findById(id);

        if (callOpt.isEmpty())
            throw new ResourceNotFoundException("I'm sorry, but the call with ID: " + id + " was not found");

        return callOpt;
    }
    @Override
    public Page<CallDTO> getProviderHistory(Long providerId, int page, int size) {

        List<States> allowedStates = List.of(
                States.REQUESTING,
                States.PENDING,
                States.FINISHED,
                States.DECLINED
        );



        Pageable pageable = PageRequest.of(page, size);

        Page<Call> calls = repository.findByProviderIdAndStateIn(providerId, allowedStates, pageable);

        return calls.map(mapper::toDto);

    }


}
