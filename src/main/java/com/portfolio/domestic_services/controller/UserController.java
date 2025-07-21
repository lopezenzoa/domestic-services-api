package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.UserDTO;
import com.portfolio.domestic_services.service.UserService;
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
@RequestMapping("/api/users")
@Tag(name = "Users", description = "CRUD Operations for users")
public class UserController {
    @Autowired private UserService service;

    @Operation(summary = "Create a new User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid User data")
    })
    @PostMapping("/create")
    public ResponseEntity<UserDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User data needed to create")
            @RequestBody UserDTO body
    ) throws UniquenessViolationException {
        Optional<UserDTO> userOpt = service.create(body);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get a full list of users",
            description = "Returns a list of users"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of users",
            content =  @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)
            )
    )
    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(
            summary = "Get a User by its ID",
            description = "Returns a User data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User data",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the User searched")
            @PathVariable Long id
    ) {
        Optional<UserDTO> userOpt = service.getById(id);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Invalid User data")
    })
    @PutMapping("/update")
    public ResponseEntity<UserDTO> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User data needed to update")
            @RequestBody UserDTO body
    ) throws UniquenessViolationException {
        Optional<UserDTO> userOpt = service.update(body);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Delete a specific User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User didn't found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the User that will be deleted")
            @PathVariable Long id
    ) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }
}
