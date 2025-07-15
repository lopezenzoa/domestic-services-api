package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/facilities")
@Tag(name = "Facilities", description = "Basic operations to create and find facilities (ADMIN only)")
public class FacilityController {
    @Autowired private FacilityService service;

    @Operation(summary = "Create a new Facility")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facility created"),
            @ApiResponse(responseCode = "400", description = "Invalid Facility data")
    })
    @PostMapping("/create")
    public ResponseEntity<FacilityDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Facility data needed to create")
            @RequestBody FacilityDTO body
    ) {
        Optional<FacilityDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get a Facility by its name",
            description = "Returns a Facility data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Facility data",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Facility not found")
    })
    @GetMapping("/search")
    public ResponseEntity<FacilityDTO> findByName(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Name of the searched Facility")
            @RequestParam String name
    ) {
        Optional<FacilityDTO> facilityOpt = service.findByName(name);
        return facilityOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
