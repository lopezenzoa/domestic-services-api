package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
}
