package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
}
