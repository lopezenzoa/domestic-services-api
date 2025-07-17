package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.ProviderDTO;
import com.portfolio.domestic_services.service.ProviderService;
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
@RequestMapping("/api/providers")
@Tag(name = "Providers", description = "CRUD Operations for providers")
public class ProviderController {
    @Autowired private ProviderService service;

    @Operation(summary = "Create a new Provider")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provider created"),
            @ApiResponse(responseCode = "400", description = "Invalid Provider data")
    })
    @PostMapping("/create")
    public ResponseEntity<ProviderDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Provider data needed to create")
            @RequestBody ProviderDTO body
    ) throws UniquenessViolationException {
        Optional<ProviderDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Update a Provider")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provider updated"),
            @ApiResponse(responseCode = "400", description = "Invalid Provider data")
    })
    @PutMapping("/update")
    public ResponseEntity<ProviderDTO> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Provider data needed to update")
            @RequestBody ProviderDTO body
    ) throws UniquenessViolationException {
        Optional<ProviderDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get a Provider by its ID",
            description = "Returns a Provider data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Provider data",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProviderDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProviderDTO> getById(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Provider searched")
            @PathVariable Long id
    ) {
        Optional<ProviderDTO> response = service.getById(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get a full list of providers",
            description = "Returns a list of providers"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of providers",
            content =  @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProviderDTO.class)
            )
    )
    @GetMapping("/")
    public ResponseEntity<List<ProviderDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Delete a specific Client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provider deleted"),
            @ApiResponse(responseCode = "404", description = "Provider didn't found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Provider that will be deleted")
            @PathVariable Long id
    ) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }
}
