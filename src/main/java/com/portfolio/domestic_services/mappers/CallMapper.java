package com.portfolio.domestic_services.mappers;

import com.portfolio.domestic_services.dto.CallDTO;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CallMapper {
    @Autowired private ProviderMapper providerMapper;
    @Autowired private ClientMapper clientMapper;

    public CallDTO toDto(Call entity) {
        return new CallDTO(
                entity.getId(),
                entity.getDescription(),
                entity.getDate().toString(),
                entity.getAddress(),
                entity.getState().toString(),
                clientMapper.toDto(entity.getClient()),
                providerMapper.toDto(entity.getProvider())
        );
    }

    public List<CallDTO> toDtoList(List<Call> entities) {
        List<CallDTO> dtos = new ArrayList<>();
        entities.forEach(entity -> dtos.add(toDto(entity)));
        return dtos;
    }

    public List<Call> toEntitiesList(List<CallDTO> dtos) {
        List<Call> entities = new ArrayList<>();
        dtos.forEach(dto -> entities.add(toEntity(dto)));
        return entities;
    }

    public Call toEntity(CallDTO dto) {
        return new Call(
                dto.getId(),
                dto.getDescription(),
                LocalDateTime.parse(dto.getDate()),
                dto.getAddress(),
                States.valueOf(dto.getState()),
                clientMapper.toEntity(dto.getClient()),
                providerMapper.toEntity(dto.getProvider())
        );
    }
}
