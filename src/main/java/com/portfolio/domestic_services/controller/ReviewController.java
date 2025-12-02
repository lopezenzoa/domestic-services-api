package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.ReviewDTO;
import com.portfolio.domestic_services.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Basic operations for the reviews of the Client")
public class ReviewController {
    @Autowired private ReviewService service;

    @Operation(summary = "Create a new Review")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Review created"),
            @ApiResponse(responseCode = "400", description = "Invalid Review data"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Review data needed to create")
            @RequestBody ReviewDTO body
    ) {
        Optional<ReviewDTO> response = service.create(body);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get a full list of reviews",
            description = "Returns a list of reviews"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of reviews",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/")
    public ResponseEntity<List<ReviewDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(
            summary = "Get all reviews of a Client",
            description = "Returns a list of all reviews of a specific Client given its id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of Client's reviews",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/client/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllByClient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Client searched")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getAllByClient(id));
    }

    @Operation(
            summary = "Get all reviews made to a Provider",
            description = "Returns a list of all reviews made to a specific Provider given its id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of Provider's reviews",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @GetMapping("/provider/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllByProvider(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Provider searched")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getAllByProvider(id));
    }
    @GetMapping("/my-reviews")
    public ResponseEntity<Page<ReviewDTO>> getMyReviewsPaged(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(service.getMyReviewsPaged(page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ReviewDTO>> getAllReviewsPaged(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(service.getAllReviewsPaged(page, size));
    }



    @Operation(summary = "Delete a specific Review")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Review deleted"),
            @ApiResponse(responseCode = "404", description = "Review didn't found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "401", description = "Request not authenticated")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID of the Review that will be deleted")
            @PathVariable Long id
    ) {
        boolean deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Gets reviews from the context's User")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of User's reviews",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
    })
    @GetMapping("/me")
    public ResponseEntity<List<ReviewDTO>> getMe() {
        return ResponseEntity.ok(service.getMe());
    }
}
