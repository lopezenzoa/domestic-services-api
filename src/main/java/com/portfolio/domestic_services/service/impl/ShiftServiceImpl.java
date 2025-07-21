package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.dto.ShiftDTO;
import com.portfolio.domestic_services.mappers.ShiftMapper;
import com.portfolio.domestic_services.model.Shift;
import com.portfolio.domestic_services.repository.ShiftRepository;
import com.portfolio.domestic_services.service.ProviderService;
import com.portfolio.domestic_services.service.ShiftService;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftServiceImpl implements ShiftService {
    @Autowired private ShiftRepository repository;
    @Autowired private ShiftMapper mapper;
    @Autowired private ProviderService providerService;

    @Override
    public Optional<ShiftDTO> create(ShiftDTO dto, Long providerId) {
        Shift entity = mapper.toEntity(dto);

        // searching the provider on db to append the shift
        Optional<ProviderDTO> providerOpt = providerService.getById(providerId);
        providerOpt.ifPresent(provider -> entity.setProvider(providerService.mapToEntity(provider)));

        // validating the date and time of the shift
        boolean isDateUnique = authenticateDateOnCreate(providerId, LocalDateTime.parse(dto.getDateTime()));

        // the date and time is not unique, so I abort the operation
        if (!isDateUnique)
            throw new UniquenessViolationException("I'm sorry but that datetime is already assigned");;

        Shift saved = repository.save(entity);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public Optional<ShiftDTO> update(ShiftDTO newDto, Long providerId) {
        Shift entity = mapper.toEntity(newDto);

        // searching the provider on db to append the shift
        Optional<ProviderDTO> providerOpt = providerService.getById(providerId);
        providerOpt.ifPresent(provider -> entity.setProvider(providerService.mapToEntity(provider)));

        // validating the date and time of the shift
        boolean isDateUnique = authenticateDateOnUpdate(providerId, LocalDateTime.parse(newDto.getDateTime()), newDto.getId());

        // validating that the shift is already associated with the provider
        Optional<Shift> shiftOpt = repository.findByIdAndProviderId(entity.getId(), providerId);

        // the date and time is not unique, so I abort the operation
        if (!isDateUnique || shiftOpt.isEmpty())
            throw new UniquenessViolationException("I'm sorry but that datetime is already assigned");;

        Shift updated = repository.save(entity);

        return Optional.of(mapper.toDto(updated));
    }

    @Override
    public List<ShiftDTO> getAllByProviderId(Long providerId) {
        providerService.getById(providerId);

        List<Shift> shifts = repository.findAllByProviderId(providerId);
        return mapper.toDtoList(shifts);
    }

    @Override
    public List<ShiftDTO> getAvailableByProviderId(Long providerId) {
        providerService.getById(providerId);

        List<Shift> shifts = repository.findAllByAvailableTrueAndProviderId(providerId);
        return mapper.toDtoList(shifts);
    }

    @Override
    public boolean delete(Long id, Long providerId) {
        // validating that the shift is already associated with the provider
        Optional<Shift> shiftOpt = repository.findByIdAndProviderId(id, providerId);

        // here the id of the shift doesn't exist or the given provider has no shifts associated then the response is false
        if (!repository.existsById(id) || shiftOpt.isEmpty())
            return false;

        repository.deleteById(id);
        return true;
    }

    // this method is to validate the date and time uniqueness for the provider's shift
    private boolean authenticateDateOnCreate(Long providerId, LocalDateTime dateTime) {
        List<Shift> shifts = repository.findAllByAvailableTrueAndDateTimeAndProviderId(dateTime, providerId);
        return shifts.isEmpty();
    }

    // see I need an extra parameter for this method because it must be excluded the Shift I want to update from the filtering
    private boolean authenticateDateOnUpdate(Long providerId, LocalDateTime dateTime, Long shiftId) {
        List<Shift> shifts = repository.findAllByAvailableTrueAndDateTimeAndProviderId(dateTime, providerId);

        // excluding the Shift that I want to update to search another Shift with the same date
        // (I guess there's a simpler way of doing this, maybe with query methods)
        List<Shift> filteredShifts = shifts.stream()
                .filter(shift -> !shift.getId().equals(shiftId))
                .toList();

        return filteredShifts.isEmpty();
    }

    // this method is intentioned to be a matcher between Provider's shifts and the requested date
    // btw, it's supposed that the Client already listed the available shifts of the Provider before doing the request for call
    @Override
    public boolean checkRequestedDate(String dtoDate, Long providerId) {
        List<ShiftDTO> availableShifts = getAvailableByProviderId(providerId);

        // the Provider has no available shifts
        if (availableShifts.isEmpty())
            return false;

        return availableShifts.stream()
                .anyMatch(shift -> shift.getDateTime().equals(dtoDate));
    }

    // this method assumes the Shift is present in the Provider's list of shifts and it's available
    @Override
    public void takeShiftOfProvider(Long providerId, LocalDateTime shiftDate) {
        List<ShiftDTO> providerShifts = getAllByProviderId(providerId);

        String shiftDateString = shiftDate.toString();

        ShiftDTO filteredShift = providerShifts.stream()
                .filter(shift -> shift.getDateTime().equals(shiftDateString)) // filtering all with the same date
                .filter(ShiftDTO::getAvailable) // filtering all available
                .toList()
                .getFirst(); // after filtering, the list of shifts would reduce to one element

        filteredShift.setAvailable(false); // taking the shift

        update(filteredShift, providerId);
    }
}
