package com.portfolio.domestic_services.mappers;

import com.portfolio.domestic_services.dto.ReviewDTO;
import com.portfolio.domestic_services.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewMapper {
    @Autowired private ClientMapper clientMapper;
    @Autowired private ProviderMapper providerMapper;

    public ReviewDTO toDto(Review entity) {
        return new ReviewDTO(
                entity.getId(),
                entity.getDescription(),
                entity.getCreationDate().toString(),
                clientMapper.toDto(entity.getClient()),
                providerMapper.toDto(entity.getProvider())
        );
    }

    public List<ReviewDTO> toDtoList(List<Review> entities) {
        List<ReviewDTO> dtos = new ArrayList<>();
        entities.forEach(entity -> dtos.add(toDto(entity)));
        return dtos;
    }

    public Review toEntity(ReviewDTO dto) {
        return new Review(
                dto.getId(),
                dto.getDescription(),
                LocalDateTime.parse(dto.getCreationDate()),
                clientMapper.toEntity(dto.getClient()),
                providerMapper.toEntity(dto.getProvider())
        );
    }
}
