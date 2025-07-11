package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findAllByProviderId(Long providerId);
    List<Shift> findAllByAvailableTrueAndProviderId(Long providerId);
    List<Shift> findAllByAvailableTrueAndDateTimeAndProviderId(LocalDateTime dateTime, Long ProviderId);
    Optional<Shift> findByIdAndProviderId(Long shiftId, Long providerId);
}
