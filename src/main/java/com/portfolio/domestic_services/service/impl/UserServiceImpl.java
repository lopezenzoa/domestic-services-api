package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.UserDTO;
import com.portfolio.domestic_services.mappers.UserMapper;
import com.portfolio.domestic_services.model.Roles;
import com.portfolio.domestic_services.model.User;
import com.portfolio.domestic_services.repository.UserRepository;
import com.portfolio.domestic_services.service.UserService;
import com.portfolio.domestic_services.service.exceptions.ResourceNotFoundException;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository repository;
    @Autowired private UserMapper mapper;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserDTO> create(UserDTO dto) throws UniquenessViolationException {
        checkFieldsUniquenessOnCreate(dto.getEmail(), dto.getPhoneNumber(), dto.getUsername());

        dto.setRole(Roles.USER); // by default, when creating a new User, its default role is USER
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User entity = mapper.toEntity(dto);
        User saved = repository.save(entity);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public Optional<UserDTO> getById(Long id) {
        Optional<User> userOpt = repository.findById(id);

        if (userOpt.isEmpty())
            throw new ResourceNotFoundException("I'm sorry, but the user with ID: " + id + " was not found");

        return Optional.of(mapper.toDto(userOpt.get()));
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> users = repository.findAll();
        return mapper.toDtoList(users);
    }

    @Override
    public Optional<UserDTO> update(UserDTO dto) throws UniquenessViolationException {
        checkFieldsUniquenessOnUpdate(dto.getId(), dto.getEmail(), dto.getPhoneNumber(), dto.getUsername());

        dto.setRole(Roles.USER); // by default, when updating a User, its default role is USER

        User entity = mapper.toEntity(dto);
        User saved = repository.save(entity);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("I'm sorry, but the user with ID: " + id + " was not found");

        repository.deleteById(id);
        return true;
    }

    @Override
    public void checkFieldsUniquenessOnCreate(String email, String phoneNumber, String username) throws UniquenessViolationException {
        boolean existsPhoneNumber = existsByPhoneNumber(phoneNumber);
        boolean existsEmail = existsByEmail(email);
        boolean existsUsername = existsByUsername(username);

        if (existsPhoneNumber || existsEmail || existsUsername)
            throw new UniquenessViolationException("I'm sorry but one of the following fields already exists in the system: email, phone number or username");
    }

    @Override
    public void checkFieldsUniquenessOnUpdate(Long userId, String email, String phoneNumber, String username) throws UniquenessViolationException {
        List<User> filteredUsers = repository.findAll()
                .stream()
                .filter(user -> !user.getId().equals(userId))
                .toList();

        // this property stores whether if there's some field repeated in the table or not
        boolean anyFieldMatch = filteredUsers.stream()
                .anyMatch(user ->
                    user.getPhoneNumber().equals(phoneNumber) ||
                    user.getEmail().equals(email) ||
                    user.getUsername().equals(username)
                );

        if (anyFieldMatch)
            throw new UniquenessViolationException("I'm sorry but one of the following fields already exists in the system: email, phone number or username");
    }

    @Override
    public Optional<UserDTO> getMe() {
        Object authUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // this getName() retrieves the email

        Optional<User> userOpt = repository.findByUsername((String) authUsername);

        return userOpt.map(user -> mapper.toDto(user));
    }

    private boolean existsByPhoneNumber(String phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber);
    }

    private boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    private boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }
}
