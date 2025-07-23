package com.portfolio.domestic_services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String email;
    private String licenseNumber; // this is going to be the difference between register a Client or a Provider
    private FacilityDTO facility;
    private String username;
    private String password;
}
