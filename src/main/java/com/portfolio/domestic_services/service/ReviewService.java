package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.ReviewDTO;
import com.portfolio.domestic_services.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Optional<ReviewDTO> create(ReviewDTO dto);
    List<ReviewDTO> getAll();
    List<ReviewDTO> getAllByClient(Long clientId);
    List<ReviewDTO> getAllByProvider(Long providerId);
    boolean delete(Long id);
    Page<ReviewDTO> getMyReviewsPaged(int page, int size);
    Page<ReviewDTO> getAllReviewsPaged(int page, int size);
    List<ReviewDTO> getMe();
}
