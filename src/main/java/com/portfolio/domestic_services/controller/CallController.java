package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.CallDTO;
import com.portfolio.domestic_services.service.CallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calls")
@Tag(name = "Calls", description = "Operations for calls")
public class CallController {
    @Autowired private CallService service;

    @Operation(summary = "Request a new Call of a Provider")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Call requested"),
            @ApiResponse(responseCode = "400", description = "Invalid request for Call"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @PostMapping("/request")
    public ResponseEntity<CallDTO> request(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The call data needed to request")
            @RequestBody CallDTO body
    ) {
        Optional<CallDTO> response = service.request(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get all calls",
            description = "Returns a list of all calls made historically"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of calls",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CallDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })

    @GetMapping("/")
    public ResponseEntity<List<CallDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(
            summary = "Get all calls of a specific Client",
            description = "Returns a list of all calls of a specific Client given its id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of Client's calls",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CallDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/client/{id}")
    public ResponseEntity<List<CallDTO>> getCallsByClient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The ID of the Client to consult")
            @Valid @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getAllByClient(id));
    }

    @Operation(
            summary = "Get all calls of a specific Provider",
            description = "Returns a list of all calls of a specific Provider given its id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of Provider's calls",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CallDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/provider/{id}")
    public ResponseEntity<List<CallDTO>> getCallsByProvider(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The ID of the Provider to consult")
            @Valid @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getAllByProvider(id));
    }

    @Operation(
            summary = "Get all requested calls of a Provider",
            description = "Returns a list of all requested calls of a specific Provider given its id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of requested Provider's calls",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CallDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/provider/{id}/requested")
    public ResponseEntity<List<CallDTO>> getRequestedCallsByProvider(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The ID of the Provider to consult")
            @Valid @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getAllRequestedByProvider(id));
    }

    @Operation(summary = "Accept a requested specific Call")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Requested Call accepted"),
            @ApiResponse(responseCode = "400", description = "Invalid path variables (maybe doesn't exist the Provider or the Call)"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/provider/{id}/history")
    public ResponseEntity<Page<CallDTO>> getProviderHistoryPaginated(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.getPaginatedByProvider(id, page, size));
    }

    @PutMapping("/provider/{providerId}/accept/{callId}")
    public ResponseEntity<Void> acceptCall(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The ID of the Provider of the requested Call")
            @Valid @PathVariable Long providerId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The ID of the Call that will be accepted")
            @Valid @PathVariable Long callId
    ) {
        boolean accepted = service.accept(providerId, callId);

        if (accepted)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Decline a requested specific Call")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Requested Call declined"),
            @ApiResponse(responseCode = "400", description = "Invalid path variables (maybe doesn't exist the Provider or the Call)"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @PutMapping("/provider/{providerId}/decline/{callId}")
    public ResponseEntity<Void> declineCall(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The ID of the Provider of the requested Call")
            @Valid @PathVariable Long providerId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The ID of the Call that will be declined")
            @Valid @PathVariable Long callId
    ) {
        boolean accepted = service.decline(providerId, callId);

        if (accepted)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Delete a specific Call")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Call deleted"),
            @ApiResponse(responseCode = "404", description = "Call didn't found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The ID of the Call that will be deleted")
            @Valid @PathVariable Long id
    ) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Gets calls from the context's User")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of User's calls",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CallDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/me")
    public ResponseEntity<List<CallDTO>> getMe() {
        return ResponseEntity.ok(service.getMe());
    }
}
