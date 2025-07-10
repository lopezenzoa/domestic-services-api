package com.portfolio.domestic_services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDTO extends ClientDTO {
    private String licenseNumber;
    private FacilityDTO facility;
    private List<ShiftDTO> shifts;
}
