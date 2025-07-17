package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.UserDTO;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDTO> create(UserDTO dto) throws UniquenessViolationException;
    Optional<UserDTO> getById(Long id);
    List<UserDTO> getAll();
    Optional<UserDTO> update(UserDTO dto) throws UniquenessViolationException ;
    boolean delete(Long id);

    void checkFieldsUniquenessOnCreate(String email, String phoneNumber, String username) throws UniquenessViolationException;
    void checkFieldsUniquenessOnUpdate(Long userId, String email, String phoneNumber, String username) throws UniquenessViolationException;
}
