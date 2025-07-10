package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.mappers.FacilityMapper;
import com.portfolio.domestic_services.mappers.ProviderMapper;
import com.portfolio.domestic_services.model.Provider;
import com.portfolio.domestic_services.repository.ProviderRepository;
import com.portfolio.domestic_services.service.FacilityService;
import com.portfolio.domestic_services.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderServiceImpl implements ProviderService {
    @Autowired private ProviderRepository repo;
    @Autowired private ProviderMapper mapper;
    @Autowired private FacilityService facilityService;
    @Autowired private FacilityMapper facilityMapper;

    @Override
    public Optional<ProviderDTO> create(ProviderDTO dto) {
        Provider provider = mapper.toEntity(dto);

        Optional<FacilityDTO> facilityOpt = facilityService.findByName(dto.getFacility().getName());

        // here, if the facility is found, then I have to map it to an entity in order to set it to the provider
        facilityOpt.ifPresent(facilityDto -> provider.setFacility(facilityMapper.toEntity(facilityDto)));
        Provider saved = repo.save(provider);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public Optional<ProviderDTO> update(ProviderDTO newDto) {
        Provider updated = repo.save(mapper.toEntity(newDto));
        return Optional.of(mapper.toDto(updated)); // Spring JPA manages automatically the update
    }

    @Override
    public Optional<ProviderDTO> getById(Long id) {
        Optional<Provider> providerOpt = repo.findById(id);
        return providerOpt.map(client -> mapper.toDto(client));
    }

    @Override
    public List<ProviderDTO> getAll() {
        return mapper.toDtoList(repo.findAll());
    }

    @Override
    public boolean delete(Long id) {
        if (!repo.existsById(id))
            return false;

        repo.deleteById(id);
        return true;
    }
}
