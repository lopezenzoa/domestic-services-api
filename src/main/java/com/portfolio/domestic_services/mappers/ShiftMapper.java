package com.portfolio.domestic_services.mappers;

import com.portfolio.domestic_services.dto.ShiftDTO;
import com.portfolio.domestic_services.model.Shift;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ShiftMapper {
    public ShiftDTO toDto(Shift entity) {
        return new ShiftDTO(
                entity.getId(),
                entity.getDateTime().toString(),
                entity.getAvailable()
        );
    }

    public List<ShiftDTO> toDtoList(List<Shift> entities) {
        List<ShiftDTO> dtos = new ArrayList<>();
        entities.forEach(entity -> dtos.add(toDto(entity)));
        return dtos;
    }

    public Shift toEntity(ShiftDTO dto) {
        return new Shift(
                dto.getId(),
                LocalDateTime.parse(dto.getDateTime()),
                dto.getAvailable(),
                null
        );
    }
}
