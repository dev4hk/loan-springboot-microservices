package com.example.entry_server.controller;

import com.example.entry_server.constants.ResultType;
import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.dto.EntryResponseDto;
import com.example.entry_server.dto.EntryUpdateResponseDto;
import com.example.entry_server.dto.ResponseDTO;
import com.example.entry_server.exception.BaseException;
import com.example.entry_server.service.IEntryService;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Entry",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE entry details"
)
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EntryController {

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
    @PostMapping("/{applicationId}")
    public ResponseDTO<EntryResponseDto> createEntry(@PathVariable Long applicationId, @Valid @RequestBody EntryRequestDto request) {
        EntryResponseDto response = entryService.create(applicationId, request);
        return ResponseDTO.ok(response);
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
    @Retry(name = "getEntry", fallbackMethod = "getEntryFallback")
    @GetMapping("/{applicationId}")
    public ResponseDTO<EntryResponseDto> getEntry(@PathVariable Long applicationId) {
        EntryResponseDto response = entryService.get(applicationId);
        return ResponseDTO.ok(response);
    }

    public ResponseDTO<EntryResponseDto> getEntryFallback(Long applicationId, Throwable throwable) {
        throw new BaseException(ResultType.SYSTEM_ERROR, "Entry server timeout", HttpStatus.INTERNAL_SERVER_ERROR);
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
    @PutMapping("/{entryId}")
    public ResponseDTO<EntryUpdateResponseDto> updateEntry(@PathVariable Long entryId, @Valid @RequestBody EntryRequestDto request) {
        EntryUpdateResponseDto response = entryService.update(entryId, request);
        return ResponseDTO.ok(response);
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
    @DeleteMapping("/{entryId}")
    public ResponseDTO<Void> deleteEntry(@PathVariable Long entryId) {
        entryService.delete(entryId);
        return ResponseDTO.ok();
    }
}
