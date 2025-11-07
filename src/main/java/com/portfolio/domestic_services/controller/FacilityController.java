package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.dto.FacilityDTO;
import com.portfolio.domestic_services.service.FacilityService;
import com.portfolio.domestic_services.service.exceptions.UniquenessViolationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/facilities")
@Tag(name = "Facilities", description = "Basic operations to create and find facilities (ADMIN only)")
public class FacilityController {
    @Autowired private FacilityService service;

    @Operation(summary = "Create a new Facility")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facility created"),
            @ApiResponse(responseCode = "400", description = "Invalid Facility data"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
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
            summary = "Get a full list of facilities",
            description = "Returns a Facility data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of facilities",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/")
    public ResponseEntity<List<FacilityDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
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
            @ApiResponse(responseCode = "404", description = "Facility not found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/search")
    public ResponseEntity<FacilityDTO> findByName(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Name of the searched Facility")
            @RequestParam String name
    ) {
        Optional<FacilityDTO> facilityOpt = service.findByName(name);
        return facilityOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get a Facility by its ID",
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
            @ApiResponse(responseCode = "404", description = "Facility not found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FacilityDTO> getById(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Facility searched")
            @PathVariable Long id
    ) {
        Optional<FacilityDTO> facilityOpt = service.getById(id);
        return facilityOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a Facility")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facility updated"),
            @ApiResponse(responseCode = "400", description = "Invalid Facility data"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @PutMapping("/update")
    public ResponseEntity<FacilityDTO> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Facility data needed to update")
            @RequestBody FacilityDTO body
    ) throws UniquenessViolationException {
        Optional<FacilityDTO> facilityOpt = service.update(body);
        return facilityOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Delete a specific Facility")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facility deleted"),
            @ApiResponse(responseCode = "404", description = "Facility didn't found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Facility that will be deleted")
            @PathVariable Long id
    ) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }
}
