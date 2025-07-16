package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
