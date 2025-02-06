package com.example.entry_server.controller;

import com.example.entry_server.constants.CommunicationStatus;
import com.example.entry_server.constants.ResultType;
import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.dto.EntryResponseDto;
import com.example.entry_server.dto.EntryUpdateResponseDto;
import com.example.entry_server.dto.ResponseDTO;
import com.example.entry_server.exception.BaseException;
import com.example.entry_server.service.IEntryService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.entry_server.dto.ResponseDTO.ok;

@Tag(
        name = "Entry",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE entry details"
)
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EntryController {

    private static final Logger logger = LoggerFactory.getLogger(EntryController.class);
    private final IEntryService entryService;

    @Operation(
            summary = "Create Entry REST API",
            description = "REST API to create new entry"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @RateLimiter(name = "createEntryRateLimiter")
    @PostMapping("/{applicationId}")
    public ResponseDTO<EntryResponseDto> createEntry(@PathVariable Long applicationId, @Valid @RequestBody EntryRequestDto request) {
        logger.info("EntryController - createEntry started");
        logger.debug("EntryController - applicationId: {}", applicationId);
        logger.debug("EntryController - request: {}", request.toString());
        EntryResponseDto response = entryService.create(applicationId, request);
        logger.info("EntryController - createEntry finished");
        return ok(response);
    }

    @Operation(
            summary = "Get Entry REST API",
            description = "REST API to get entry"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @RateLimiter(name = "getEntryRateLimiter")
    @Retry(name = "getEntry")
    @GetMapping("/{applicationId}")
    public ResponseDTO<EntryResponseDto> getEntry(@PathVariable Long applicationId) {
        logger.info("EntryController - getEntry started");
        logger.debug("EntryController - applicationId: {}", applicationId);
        EntryResponseDto response = entryService.get(applicationId);
        logger.info("EntryController - getEntry finished");
        return ok(response);
    }

    @Operation(
            summary = "Update Entry REST API",
            description = "REST API to update entry"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @RateLimiter(name = "updateEntryRateLimiter")
    @PutMapping("/{entryId}")
    public ResponseDTO<EntryUpdateResponseDto> updateEntry(@PathVariable Long entryId, @Valid @RequestBody EntryRequestDto request) {
        logger.info("EntryController - updateEntry started");
        logger.debug("EntryController - entryId: {}", entryId);
        logger.debug("EntryController - request: {}", request.toString());
        EntryUpdateResponseDto response = entryService.update(entryId, request);
        logger.info("EntryController - updateEntry finished");
        return ok(response);
    }

    @Operation(
            summary = "Delete Entry REST API",
            description = "REST API to delete entry"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @RateLimiter(name = "deleteEntryRateLimiter")
    @DeleteMapping("/{entryId}")
    public ResponseDTO<Void> deleteEntry(@PathVariable Long entryId) {
        logger.info("EntryController - deleteEntry started");
        logger.debug("EntryController - entryId: {}", entryId);
        entryService.delete(entryId);
        logger.info("EntryController - deleteEntry finished");
        return ok();
    }

    @Operation(
            summary = "Get Stats REST API",
            description = "REST API to get stats"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @RateLimiter(name = "getStatsRateLimiter")
    @GetMapping("/stats")
    public ResponseDTO<Map<CommunicationStatus, Long>> getStats() {
        logger.info("EntryController - getStats started");
        Map<CommunicationStatus, Long> stats = entryService.getEntryStatistics();
        logger.info("EntryController - getStats finished");
        return ok(stats);
    }

}
