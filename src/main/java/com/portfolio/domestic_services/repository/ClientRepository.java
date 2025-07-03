package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
