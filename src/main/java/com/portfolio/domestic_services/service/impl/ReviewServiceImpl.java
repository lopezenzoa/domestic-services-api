package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.*;
import com.portfolio.domestic_services.mappers.ReviewMapper;
import com.portfolio.domestic_services.model.Review;
import com.portfolio.domestic_services.model.Roles;
import com.portfolio.domestic_services.repository.ReviewRepository;
import com.portfolio.domestic_services.service.ClientService;
import com.portfolio.domestic_services.service.ProviderService;
import com.portfolio.domestic_services.service.ReviewService;
import com.portfolio.domestic_services.service.UserService;
import com.portfolio.domestic_services.service.exceptions.EmptyCollectionException;
import com.portfolio.domestic_services.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired private ReviewRepository repository;
    @Autowired private ReviewMapper mapper;
    @Autowired private ProviderService providerService;
    @Autowired private ClientService clientService;
    @Autowired private UserService userService;

    @Override
    public Optional<ReviewDTO> create(ReviewDTO dto) {
        // searching the provider
        Optional<ProviderDTO> providerOpt = providerService.getById(dto.getProvider().getId());

        // searching the client
        Optional<ClientDTO> clientOpt = clientService.getById(dto.getClient().getId());

        // checking the presence of both client and provider
        if (clientOpt.isEmpty() || providerOpt.isEmpty())
            return Optional.empty();

        dto.setClient(clientOpt.get());
        dto.setProvider(providerOpt.get());
        dto.setCreationDate(LocalDateTime.now().toString());

        Review review = mapper.toEntity(dto);
        Review saved = repository.save(review);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public List<ReviewDTO> getAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public List<ReviewDTO> getAllByClient(Long clientId) {
        clientService.getById(clientId);
        return mapper.toDtoList(repository.findAllByClientId(clientId));
    }

    @Override
    public List<ReviewDTO> getAllByProvider(Long providerId) {
        providerService.getById(providerId);
        return mapper.toDtoList(repository.findAllByProviderId(providerId));
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("I'm sorry, but the review with ID: " + id + " was not found");

        repository.deleteById(id);
        return true;
    }

    @Override
    public List<ReviewDTO> getMe() {
        Optional<UserDTO> userOpt = userService.getMe();

        if (userOpt.isPresent() && userOpt.get().getRole().equals(Roles.CLIENT))
            return getAllByClient(userOpt.get().getId());

        if (userOpt.isPresent() && userOpt.get().getRole().equals(Roles.PROVIDER))
            return getAllByProvider(userOpt.get().getId());

        throw new EmptyCollectionException("I'm sorry, you don't have any reviews");
    }
}
