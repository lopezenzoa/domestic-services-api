package com.portfolio.domestic_services.mappers;

import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.model.Facility;
import com.portfolio.domestic_services.service.exceptions.EmptyCollectionException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FacilityMapper {
    public FacilityDTO toDto(Facility entity) {
        return new FacilityDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }

    public List<FacilityDTO> toDtoList(List<Facility> entities) {
        List<FacilityDTO> dtos = new ArrayList<>();
        entities.forEach(entity -> dtos.add(toDto(entity)));
        
        return dtos;
    }

    public Facility toEntity(FacilityDTO dto) {
        return new Facility(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                null
        );
    }
}
