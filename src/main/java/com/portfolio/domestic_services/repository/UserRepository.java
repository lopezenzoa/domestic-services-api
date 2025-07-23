package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
