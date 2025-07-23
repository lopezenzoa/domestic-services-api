package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.dto.LoginDTO;
import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.dto.RegisterDTO;
import com.portfolio.domestic_services.security.JwtUtil;
import com.portfolio.domestic_services.service.AuthService;
import com.portfolio.domestic_services.service.ClientService;
import com.portfolio.domestic_services.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired private ProviderService providerService;
    @Autowired private ClientService clientService;

    @Autowired private JwtUtil jwtUtils;
    @Autowired private AuthenticationManager authenticationManager;

    @Override
    public Optional<?> register(RegisterDTO dto) {
        if (dto.getLicenseNumber() == null) {
            // then I have to register a new Client
            ClientDTO clientDTO = new ClientDTO();

            clientDTO.setFirstName(dto.getFirstName());
            clientDTO.setLastName(dto.getLastName());
            clientDTO.setAddress(dto.getAddress());
            clientDTO.setPhoneNumber(dto.getPhoneNumber());
            clientDTO.setEmail(dto.getEmail());
            clientDTO.setUsername(dto.getUsername());
            clientDTO.setPassword(dto.getPassword());

            return clientService.create(clientDTO);
        } else {
            // then I have to register a new Provider
            ProviderDTO providerDTO = new ProviderDTO();

            providerDTO.setFirstName(dto.getFirstName());
            providerDTO.setLastName(dto.getLastName());
            providerDTO.setAddress(dto.getAddress());
            providerDTO.setPhoneNumber(dto.getPhoneNumber());
            providerDTO.setEmail(dto.getEmail());
            providerDTO.setLicenseNumber(dto.getLicenseNumber());
            providerDTO.setFacility(dto.getFacility());
            providerDTO.setUsername(dto.getUsername());
            providerDTO.setPassword(dto.getPassword());

            return providerService.create(providerDTO);
        }
    }

    @Override
    public Optional<String> login(LoginDTO dto) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authentication = authenticationManager.authenticate(login);

        String jwt = jwtUtils.create(dto.getUsername());
        return Optional.of(jwt);
    }
}
