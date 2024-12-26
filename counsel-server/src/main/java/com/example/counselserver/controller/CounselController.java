package com.example.counselserver.controller;

import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;
import com.example.counselserver.dto.ResponseDTO;
import com.example.counselserver.service.ICounselService;
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

import static com.example.counselserver.dto.ResponseDTO.ok;

@Tag(
        name = "CRUD REST APIs for Counsel",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE counsel details"
)
@Validated
@RequiredArgsConstructor
@RequestMapping("/counsels")
@RestController
public class CounselController {

    private final ICounselService counselService;

    @Operation(
            summary = "Create Counsel REST API",
            description = "REST API to create new counsel"
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
    public ResponseDTO<CounselResponseDto> create(@Valid @RequestBody CounselRequestDto request) {
        return ok(counselService.create(request));
    }

    @Operation(
            summary = "Get Counsel REST API",
            description = "REST API to get counsel"
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
    @GetMapping("/{counselId}")
    public ResponseDTO<CounselResponseDto> get(@PathVariable Long counselId) {
        return ok(counselService.get(counselId));
    }

    @Operation(
            summary = "Update Counsel REST API",
            description = "REST API to update counsel"
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
    @PutMapping("/{counselId}")
    public ResponseDTO<CounselResponseDto> update(@PathVariable Long counselId, @Valid @RequestBody CounselRequestDto request) {
        return ok(counselService.update(counselId, request));
    }

    @Operation(
            summary = "Delete Counsel REST API",
            description = "REST API to delete counsel"
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
    @DeleteMapping("/{counselId}")
    public ResponseDTO<CounselResponseDto> delete(@PathVariable Long counselId) {
        counselService.delete(counselId);
        return ok();
    }

}
