package com.example.applicationserver.controller;

import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.service.IApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.applicationserver.dto.ResponseDTO.ok;

@Tag(
        name = "CRUD REST APIs for Application",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE application details"
)
@Validated
@RequiredArgsConstructor
@RequestMapping("/applications")
@RestController
public class ApplicationController {

    private final IApplicationService applicationService;

    @Operation(
            summary = "Create Application REST API",
            description = "REST API to create new application"
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
    @PostMapping
    public ResponseDTO<ApplicationResponseDto> create(@Valid @RequestBody ApplicationRequestDto request) {
        return ok(applicationService.create(request));
    }

    @Operation(
            summary = "Get Application REST API",
            description = "REST API to get application"
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
    @GetMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId) {
        return ok(applicationService.get(applicationId));
    }

    @Operation(
            summary = "Update Application REST API",
            description = "REST API to update application"
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
    @PutMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> update(@PathVariable Long applicationId, @Valid @RequestBody ApplicationRequestDto request) {
        return ok(applicationService.update(applicationId, request));
    }

    @Operation(
            summary = "Delete Application REST API",
            description = "REST API to delete application"
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
    @DeleteMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> delete(@PathVariable Long applicationId) {
        applicationService.delete(applicationId);
        return ok();
    }

}
