package com.portfolio.domestic_services.mappers;

import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.model.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProviderMapper {
    @Autowired private FacilityMapper facilityMapper;
    @Autowired private ShiftMapper shiftMapper;

    public ProviderDTO toDto(Provider entity) {
        ProviderDTO dto = new ProviderDTO();

        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());

        dto.setLicenseNumber(entity.getLicenseNumber());
        dto.setFacility(facilityMapper.toDto(entity.getFacility()));
        dto.setShifts(entity.getShifts() == null ? new ArrayList<>() : shiftMapper.toDtoList(entity.getShifts()));

        return dto;
    }

    public List<ProviderDTO> toDtoList(List<Provider> entities) {
        List<ProviderDTO> dtos = new ArrayList<>();
        entities.forEach(entity -> dtos.add(toDto(entity)));
        return dtos;
    }

    public Provider toEntity(ProviderDTO dto) {
        Provider provider = new Provider();

        provider.setId(dto.getId());
        provider.setFirstName(dto.getFirstName());
        provider.setLastName(dto.getLastName());
        provider.setAddress(dto.getAddress());
        provider.setPhoneNumber(dto.getPhoneNumber());
        provider.setEmail(dto.getEmail());
        provider.setUsername(dto.getUsername());
        provider.setPassword(dto.getPassword());
        provider.setRole(dto.getRole());

        provider.setLicenseNumber(dto.getLicenseNumber());
        provider.setFacility(facilityMapper.toEntity(dto.getFacility()));

        return provider;


    }
}
