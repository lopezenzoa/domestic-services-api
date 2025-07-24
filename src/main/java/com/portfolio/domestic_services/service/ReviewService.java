package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.ReviewDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Optional<ReviewDTO> create(ReviewDTO dto);
    List<ReviewDTO> getAll();
    List<ReviewDTO> getAllByClient(Long clientId);
    List<ReviewDTO> getAllByProvider(Long providerId);
    boolean delete(Long id);

    List<ReviewDTO> getMe();
}
