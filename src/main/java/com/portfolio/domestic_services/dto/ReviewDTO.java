package com.portfolio.domestic_services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private String description;
    private String creationDate;
    private ClientDTO client;
    private ProviderDTO provider;
}
