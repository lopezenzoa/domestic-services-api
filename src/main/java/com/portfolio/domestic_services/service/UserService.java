package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDTO> create(UserDTO dto);
    Optional<UserDTO> getById(Long id);
    List<UserDTO> getAll();
    Optional<UserDTO> update(UserDTO dto);
    boolean delete(Long id);
}
