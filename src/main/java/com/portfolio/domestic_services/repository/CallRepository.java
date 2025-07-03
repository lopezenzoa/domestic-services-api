package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<Call, Long> {
}
