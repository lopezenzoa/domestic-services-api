package com.portfolio.domestic_services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlagDTO {
    private Long id;
    private String reason;
    private String reportDate;
    private ClientDTO client;
    private ProviderDTO provider;
}
