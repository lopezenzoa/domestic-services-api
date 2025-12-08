package com.portfolio.domestic_services.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO {


    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$",
            message = "El nombre solo puede contener letras y espacios"
    )
    private String name;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 5, max = 100, message = "La descripción debe tener entre 5 y 100 caracteres")
    private String description;
}
