package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByClientId(Long clientId);
    List<Review> findAllByProviderId(Long providerId);
}
