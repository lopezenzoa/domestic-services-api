package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.UserDTO;
import com.portfolio.domestic_services.mappers.UserMapper;
import com.portfolio.domestic_services.model.Roles;
import com.portfolio.domestic_services.model.User;
import com.portfolio.domestic_services.repository.UserRepository;
import com.portfolio.domestic_services.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository repository;
    @Autowired private UserMapper mapper;

    @Override
    public Optional<UserDTO> create(UserDTO dto) {
        dto.setRole(Roles.USER); // by default, when creating a new User, its default role is USER

        User entity = mapper.toEntity(dto);
        User saved = repository.save(entity);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public Optional<UserDTO> getById(Long id) {
        Optional<User> userOpt = repository.findById(id);
        return userOpt.map(user -> mapper.toDto(user));
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> users = repository.findAll();
        return mapper.toDtoList(users);
    }

    @Override
    public Optional<UserDTO> update(UserDTO dto) {
        dto.setRole(Roles.USER); // by default, when updating a User, its default role is USER

        User entity = mapper.toEntity(dto);
        User saved = repository.save(entity);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id))
            return false;

        repository.deleteById(id);
        return true;
    }
}
