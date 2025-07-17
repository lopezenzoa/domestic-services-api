package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.dto.ShiftDTO;
import com.portfolio.domestic_services.mappers.ProviderMapper;
import com.portfolio.domestic_services.mappers.ShiftMapper;
import com.portfolio.domestic_services.model.Shift;
import com.portfolio.domestic_services.repository.ShiftRepository;
import com.portfolio.domestic_services.service.ProviderService;
import com.portfolio.domestic_services.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftServiceImpl implements ShiftService {
    @Autowired private ShiftRepository repo;
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
            return Optional.empty();

        Shift saved = repo.save(entity);

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
        Optional<Shift> shiftOpt = repo.findByIdAndProviderId(newDto.getId(), providerId);

        // the date and time is not unique, so I abort the operation
        if (!isDateUnique || shiftOpt.isEmpty())
            return Optional.empty();

        Shift updated = repo.save(entity);

        return Optional.of(mapper.toDto(updated));
    }

    @Override
    public List<ShiftDTO> getAllByProviderId(Long providerId) {
        List<Shift> shifts = repo.findAllByProviderId(providerId);
        return mapper.toDtoList(shifts);
    }

    @Override
    public List<ShiftDTO> getAvailableByProviderId(Long providerId) {
        List<Shift> shifts = repo.findAllByAvailableTrueAndProviderId(providerId);
        return mapper.toDtoList(shifts);
    }

    @Override
    public boolean delete(Long id, Long providerId) {
        // validating that the shift is already associated with the provider
        Optional<Shift> shiftOpt = repo.findByIdAndProviderId(id, providerId);

        // here the id of the shift doesn't exist or the given provider has no shifts associated then the response is false
        if (!repo.existsById(id) || shiftOpt.isEmpty())
            return false;

        repo.deleteById(id);
        return true;
    }

    // this method is to validate the date and time uniqueness for the provider's shift
    private boolean authenticateDate(Long providerId, LocalDateTime dateTime) {
        List<Shift> shifts = repo.findAllByAvailableTrueAndDateTimeAndProviderId(dateTime, providerId);
        return shifts.isEmpty();
    }
}
