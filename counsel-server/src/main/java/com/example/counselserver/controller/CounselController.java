package com.example.counselserver.controller;

import com.example.counselserver.constants.CommunicationStatus;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.counselserver.dto.ResponseDTO.ok;

@Tag(
        name = "Counsel",
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
        logger.info("CounselController - create started");
        logger.debug("CounselController - request: {}", request.toString());
        CounselResponseDto counselResponseDto = counselService.create(request);
        logger.info("CounselController - create finished");
        return ok(counselResponseDto);
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
        logger.info("CounselController - get started");
        logger.debug("CounselController - counselId: {}", counselId);
        CounselResponseDto counselResponseDto = counselService.get(counselId);
        logger.info("CounselController - get finished");
        return ok(counselResponseDto);
    }

    @Operation(
            summary = "Get Counsel by Email REST API",
            description = "REST API to get counsel by email"
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
    @RateLimiter(name = "getByEmailRateLimiter")
    @GetMapping("/email")
    public ResponseDTO<CounselResponseDto> getByEmail(@RequestParam("email") String email) {
        logger.info("CounselController - getByEmail started");
        logger.debug("CounselController - email: {}", email);
        CounselResponseDto counselResponseDto = counselService.getByEmail(email);
        logger.info("CounselController - getByEmail finished");
        return ok(counselResponseDto);
    }

    @Operation(
            summary = "Get All Counsel REST API",
            description = "REST API to get all counsel"
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
    @RateLimiter(name = "getAllRateLimiter")
    @GetMapping
    public ResponseDTO<Page<CounselResponseDto>> getAll(Pageable pageable) {
        logger.info("CounselController - getAll started");
        Page<CounselResponseDto> response = counselService.getAll(pageable);
        logger.info("CounselController - getAll finished");
        return ok(response);
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
        logger.info("CounselController - update started");
        logger.debug("CounselController - counselId: {}", counselId);
        logger.debug("CounselController - request: {}", request.toString());
        CounselResponseDto update = counselService.update(counselId, request);
        logger.info("CounselController - update finished");
        return ok(update);
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
        logger.info("CounselController - delete started");
        logger.debug("CounselController - counselId: {}", counselId);
        counselService.delete(counselId);
        logger.info("CounselController - delete finished");
        return ok();
    }

    @Operation(
            summary = "Complete Counsel REST API",
            description = "REST API to complete counsel"
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
    @RateLimiter(name = "completeRateLimiter")
    @PatchMapping("/{counselId}")
    public ResponseDTO<Void> complete(@PathVariable Long counselId) {
        logger.info("CounselController - complete started");
        logger.debug("CounselController - counselId: {}", counselId);
        counselService.complete(counselId);
        logger.info("CounselController - complete finished");
        return ok();
    }

    public ResponseDTO<Map<CommunicationStatus, Long>> getStats() {
        logger.info("CounselController - getStats started");
        Map<CommunicationStatus, Long> stats = counselService.getCounselStatistics();
        logger.info("CounselController - getStats finished");
        return ok(stats);
    }

}
