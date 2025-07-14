package com.portfolio.domestic_services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallDTO {
    private Long id;
    private String description;
    private String date;
    private String address;
    private String state;
    private ClientDTO client;
    private ProviderDTO provider;
}
