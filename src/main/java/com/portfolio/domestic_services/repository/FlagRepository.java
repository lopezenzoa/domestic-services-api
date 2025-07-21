package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Flag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlagRepository extends JpaRepository<Flag, Long> {
    List<Flag> findAllByProviderId(Long providerId);
    List<Flag> findAllByClientId(Long clientId);
}
