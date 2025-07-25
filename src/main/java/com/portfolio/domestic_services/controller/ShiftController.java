package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.ShiftDTO;
import com.portfolio.domestic_services.service.ShiftService;
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
@RequestMapping("/api/providers/shifts")
@Tag(name = "Shifts", description = "Basic operations for the shifts of the Provider")
public class ShiftController {
    @Autowired private ShiftService service;

    @Operation(summary = "Create a new Shift")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shift created"),
            @ApiResponse(responseCode = "400", description = "Invalid Shift data"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    // I have to think a way of build URLs in a cleaner format, since this is show as follows: /api/providers/shifts/{6, 7, 8, ...}/create
    @PostMapping("/{providerId}/create")
    public ResponseEntity<ShiftDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Shift data needed to create")
            @RequestBody ShiftDTO body,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Provider owner of the Shift")
            @PathVariable Long providerId
    ) {
        Optional<ShiftDTO> response = service.create(body, providerId);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get all shifts of a Provider",
            description = "Returns a list of all shifts of a specific Provider given its id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of Provider's shifts",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ShiftDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/{providerId}")
    public ResponseEntity<List<ShiftDTO>> getAllByProviderId(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Provider owner of the shifts")
            @PathVariable Long providerId
    ) {
        return ResponseEntity.ok(service.getAllByProviderId(providerId));
    }

    @Operation(
            summary = "Get all available shifts of a Provider",
            description = "Returns a list of all available shifts of a specific Provider given its id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of Provider's available shifts",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ShiftDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/{providerId}/available")
    public ResponseEntity<List<ShiftDTO>> getAvailableByProviderId(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Provider owner of the available shifts")
            @PathVariable Long providerId
    ) {
        return ResponseEntity.ok(service.getAvailableByProviderId(providerId));
    }

    @Operation(summary = "Update a Shift")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shift updated"),
            @ApiResponse(responseCode = "400", description = "Invalid Shift data"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @PutMapping("/{providerId}/update")
    public ResponseEntity<ShiftDTO> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Shift data needed to update")
            @RequestBody ShiftDTO body,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Provider owner of the Shift that will be updated")
            @PathVariable Long providerId
    ) {
        Optional<ShiftDTO> response = service.update(body, providerId);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Delete a specific Shift of a Provider")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shift deleted"),
            @ApiResponse(responseCode = "404", description = "Shift didn't found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @DeleteMapping("/{providerId}/delete/{shiftId}")
    public ResponseEntity<Void> delete(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Provider owner of the Shift that will be deleted")
            @PathVariable Long providerId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Shift that will be deleted")
            @PathVariable Long shiftId
    ) {
        boolean deleted = service.delete(shiftId, providerId);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }


    @Operation(summary = "Gets shifts from the context's Provider")
    @ApiResponse(
            responseCode = "200",
            description = "List of Provider's shifts",
            content =  @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ShiftDTO.class)
            )
    )
    @GetMapping("/me")
    public ResponseEntity<List<ShiftDTO>> getMe() {
        return ResponseEntity.ok(service.getMe());
    }
}
