package com.portfolio.domestic_services.mappers;

import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.model.Facility;
import org.springframework.stereotype.Component;

@Component
public class FacilityMapper {
    public FacilityDTO toDto(Facility entity) {
        return new FacilityDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
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
