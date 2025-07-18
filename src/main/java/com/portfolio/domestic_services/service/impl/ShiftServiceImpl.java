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
        boolean isDateUnique = authenticateDate(providerId, LocalDateTime.parse(dto.getDateTime()));

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
        boolean isDateUnique = authenticateDate(providerId, LocalDateTime.parse(newDto.getDateTime()));

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
    private boolean authenticateDate(Long providerId, LocalDateTime dateTime) {
        List<Shift> shifts = repository.findAllByAvailableTrueAndDateTimeAndProviderId(dateTime, providerId);
        return shifts.isEmpty();
    }
}
