package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.CallDTO;
import com.portfolio.domestic_services.dto.ChatListDTO;
import com.portfolio.domestic_services.mappers.CallMapper;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.User;
import com.portfolio.domestic_services.service.CallService;
import com.portfolio.domestic_services.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calls")

@Tag(name = "Calls", description = "Operations for calls")
public class CallController {
    @Autowired private CallService service;
    @Autowired
    private UserService userService;
    @Autowired
    private CallMapper mapper;

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

    @GetMapping("/history")
    public Page<CallDTO> getProviderHistory(
            @RequestParam Long providerId,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return service.getProviderHistory(providerId, state, start, end, page, size);
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
    @Operation(summary = "Mark a specific Call as finished")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Call marked as finished"),
            @ApiResponse(responseCode = "400", description = "Invalid path variables"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @PutMapping("/provider/{providerId}/finish/{callId}")
    public ResponseEntity<Void> finishCall(
            @PathVariable Long providerId,
            @PathVariable Long callId
    ) {
        boolean finished = service.finish(providerId, callId);

        if (finished)
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/provider/{providerId}/detail/{callId}")
    public ResponseEntity<CallDTO> getCallDetail(
            @PathVariable Long providerId,
            @PathVariable Long callId
    ) {
        Optional<CallDTO> result = service.getProviderCallDetail(providerId, callId);

        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    @GetMapping("/client/call/{id}")
    public ResponseEntity<?> getCallForClient(@PathVariable Long id) {

        // Obtener username autenticado
        String username = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Obtener usuario desde la base
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long clientId = user.getId();

        // Buscar el turno con relaciones cargadas
        Call call = service.findById(id);

        // Seguridad: verificar que el turno pertenezca al cliente autenticado
        if (!call.getClient().getId().equals(clientId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes ver esta llamada");
        }

        // DEVOLVER DTO
        return ResponseEntity.ok(mapper.toDto(call));
    }

    @GetMapping("/chats")
    public List<ChatListDTO> getMyChats() {
        return service.getMyChats();
    }


}
