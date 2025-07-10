package com.portfolio.domestic_services.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String email;
    // private List<ReviewDTO> reviews;
    // private List<FavoritesDTO> favorites;
    @JsonIgnore private List<CallDTO> calls;

    /* Credentials section */
    private String username;
    private String password;
}
