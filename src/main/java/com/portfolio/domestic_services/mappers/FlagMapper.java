package com.portfolio.domestic_services.mappers;

import com.portfolio.domestic_services.dto.FlagDTO;
import com.portfolio.domestic_services.model.Flag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlagMapper {
    @Autowired private ClientMapper clientMapper;
    @Autowired private ProviderMapper providerMapper;

    public FlagDTO toDto(Flag entity) {
        FlagDTO dto = new FlagDTO();

        dto.setId(entity.getId());
        dto.setReason(entity.getReason());
        dto.setReportDate(entity.getReportDate().toString());
        dto.setClient(clientMapper.toDto(entity.getClient()));
        dto.setProvider(providerMapper.toDto(entity.getProvider()));

        return dto;
    }

    public List<FlagDTO> toDtoList(List<Flag> entities) {
        List<FlagDTO> dtos = new ArrayList<>();
        entities.forEach(entity -> dtos.add(toDto(entity)));

        return dtos;
    }

    public Flag toEntity(FlagDTO dto) {
        Flag entity = new Flag();

        entity.setId(dto.getId());
        entity.setReason(dto.getReason());
        entity.setReportDate(LocalDateTime.parse(dto.getReportDate()));
        entity.setClient(clientMapper.toEntity(dto.getClient()));
        entity.setProvider(providerMapper.toEntity(dto.getProvider()));

        return entity;
    }
}
