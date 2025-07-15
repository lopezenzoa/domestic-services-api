package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.ClientDTO;
import com.portfolio.domestic_services.service.ClientService;
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
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "CRUD Operations for clients")
public class ClientController {
    @Autowired
    private ClientService service;

    @Operation(summary = "Create a new Client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client created"),
            @ApiResponse(responseCode = "400", description = "Invalid Client data")
    })
    @PostMapping("/create")
    public ResponseEntity<ClientDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Client data needed to create")
            @RequestBody ClientDTO body
    ) {
        Optional<ClientDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Update a Client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client updated"),
            @ApiResponse(responseCode = "400", description = "Invalid Client data")
    })
    @PutMapping("/update")
    public ResponseEntity<ClientDTO> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Client data needed to update")
            @RequestBody ClientDTO body
    ) {
        Optional<ClientDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get a Client by its ID",
            description = "Returns a Client data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Client data",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClientDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getById(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Client searched")
            @PathVariable Long id
    ) {
        Optional<ClientDTO> response = service.getById(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get a full list of clients",
            description = "Returns a list of clients"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of clients",
            content =  @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientDTO.class)
            )
    )
    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Delete a specific Client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client deleted"),
            @ApiResponse(responseCode = "404", description = "Client didn't found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Client that will be deleted")
            @PathVariable Long id
    ) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }
}
