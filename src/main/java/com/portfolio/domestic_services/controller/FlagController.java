package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.FlagDTO;
import com.portfolio.domestic_services.service.FlagService;
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
@RequestMapping("/api/flags")
@Tag(name = "Flags", description = "Basic operations for the flags of the Client")
public class FlagController {
    @Autowired private FlagService service;

    @Operation(summary = "Create a new Flag")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flag created"),
            @ApiResponse(responseCode = "400", description = "Invalid Flag data"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @PostMapping("/create")
    public ResponseEntity<FlagDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Flag data needed to create")
            @RequestBody FlagDTO body
    ) {
        Optional<FlagDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get a full list of flags",
            description = "Returns a list of flags"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of flags",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FlagDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/")
    public ResponseEntity<List<FlagDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(
            summary = "Get all reviews of a Provider",
            description = "Returns a list of all flags of a specific Provider given its id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of Provider's flags",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FlagDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<FlagDTO>> getByProvider(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Provider searched")
            @PathVariable Long providerId
    ) {
        return ResponseEntity.ok(service.getByProvider(providerId));
    }

    @Operation(
            summary = "Get all flags of a Client",
            description = "Returns a list of all flags of a specific Client given its id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of Client's flags",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FlagDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<FlagDTO>> getByClient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Client searched")
            @PathVariable Long clientId
    ) {
        return ResponseEntity.ok(service.getByClient(clientId));
    }

    @Operation(summary = "Delete a specific Flag")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flag deleted"),
            @ApiResponse(responseCode = "404", description = "Flag didn't found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Flag that will be deleted")
            @PathVariable Long id
    ) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Gets flags from the context's User")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of User's flags",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FlagDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/me")
    public ResponseEntity<List<FlagDTO>> getMe() {
        return ResponseEntity.ok(service.getMe());
    }
}
