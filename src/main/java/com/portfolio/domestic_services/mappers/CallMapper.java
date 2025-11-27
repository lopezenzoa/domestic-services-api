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
                providerMapper.toDto(entity.getProvider()),
                entity.getCost(),
                entity.getReview()
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
        Call call = new Call();

        call.setId(dto.getId());
        call.setDescription(dto.getDescription());
        call.setDate(LocalDateTime.parse(dto.getDate()));
        call.setAddress(dto.getAddress());
        call.setState(States.valueOf(dto.getState()));

        if (dto.getClient() != null)
            call.setClient(clientMapper.toEntity(dto.getClient()));

        if (dto.getProvider() != null)
            call.setProvider(providerMapper.toEntity(dto.getProvider()));

        return call;
    }

}
