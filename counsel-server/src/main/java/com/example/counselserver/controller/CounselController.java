package com.example.counselserver.controller;

import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;
import com.example.counselserver.dto.ResponseDTO;
import com.example.counselserver.service.ICounselService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.counselserver.dto.ResponseDTO.ok;

@Tag(
        name = "CRUD REST APIs for Counsel",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE counsel details"
)
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CounselController {

    private static final Logger logger = LoggerFactory.getLogger(CounselController.class);
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
    @RateLimiter(name = "createRateLimiter")
    @PostMapping
    public ResponseDTO<CounselResponseDto> create(@Valid @RequestBody CounselRequestDto request) {
        logger.info("CounselController - create invoked");
        logger.debug("CounselController - request: {}", request.toString());
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
    @RateLimiter(name = "getRateLimiter")
    @GetMapping("/{counselId}")
    public ResponseDTO<CounselResponseDto> get(@PathVariable Long counselId) {
        logger.info("CounselController - get invoked");
        logger.debug("CounselController - counselId: {}", counselId);
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
    @RateLimiter(name = "updateRateLimiter")
    @PutMapping("/{counselId}")
    public ResponseDTO<CounselResponseDto> update(@PathVariable Long counselId, @Valid @RequestBody CounselRequestDto request) {
        logger.info("CounselController - update invoked");
        logger.debug("CounselController - counselId: {}", counselId);
        logger.debug("CounselController - request: {}", request.toString());
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
    @RateLimiter(name = "deleteRateLimiter")
    @DeleteMapping("/{counselId}")
    public ResponseDTO<CounselResponseDto> delete(@PathVariable Long counselId) {
        logger.info("CounselController - delete invoked");
        logger.debug("CounselController - counselId: {}", counselId);
        counselService.delete(counselId);
        return ok();
    }

}
