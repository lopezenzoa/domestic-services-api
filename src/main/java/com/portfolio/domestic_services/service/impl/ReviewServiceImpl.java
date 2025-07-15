package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.dto.ReviewDTO;
import com.portfolio.domestic_services.mappers.ReviewMapper;
import com.portfolio.domestic_services.model.Review;
import com.portfolio.domestic_services.repository.ReviewRepository;
import com.portfolio.domestic_services.service.ClientService;
import com.portfolio.domestic_services.service.ProviderService;
import com.portfolio.domestic_services.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired private ReviewRepository repo;
    @Autowired private ReviewMapper mapper;
    @Autowired private ProviderService providerService;
    @Autowired private ClientService clientService;

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
        Review saved = repo.save(review);

        return Optional.of(mapper.toDto(saved));
    }

    @Override
    public List<ReviewDTO> getAll() {
        return mapper.toDtoList(repo.findAll());
    }

    @Override
    public List<ReviewDTO> getAllByClient(Long clientId) {
        return mapper.toDtoList(repo.findAllByClientId(clientId));
    }

    @Override
    public List<ReviewDTO> getAllByProvider(Long providerId) {
        return mapper.toDtoList(repo.findAllByProviderId(providerId));
    }

    @Override
    public boolean delete(Long id) {
        if (!repo.existsById(id))
            return false;

        repo.deleteById(id);
        return true;
    }
}
