package com.portfolio.domestic_services.mappers;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.model.Roles;
import com.portfolio.domestic_services.model.Client;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientMapper {
    public ClientDTO toDto(Client entity) {
        return new ClientDTO(
            entity.getId(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getAddress(),
            entity.getPhoneNumber(),
            entity.getEmail(),
            null,
            entity.getUsername(),
            entity.getPassword()
        );
    }

    public List<ClientDTO> toDtoList(List<Client> entities) {
        List<ClientDTO> dtos = new ArrayList<>();
        entities.forEach(entity -> dtos.add(toDto(entity)));
        return dtos;
    }

    public Client toEntity(ClientDTO dto, Roles role) {
        return new Client(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getAddress(),
                dto.getPhoneNumber(),
                dto.getEmail(),
                null,
                dto.getUsername(),
                dto.getPassword(),
                role
        );
    }
}
