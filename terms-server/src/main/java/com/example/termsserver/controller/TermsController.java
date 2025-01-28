package com.example.termsserver.controller;

import com.example.termsserver.constants.ResultType;
import com.example.termsserver.dto.ResponseDTO;
import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.exception.BaseException;
import com.example.termsserver.service.ITermsService;
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

import java.util.List;

import static com.example.termsserver.dto.ResponseDTO.ok;

@Tag(
        name = "Terms",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE terms details"
)
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TermsController {

    private static final Logger logger = LoggerFactory.getLogger(TermsController.class);
    private final ITermsService termsService;

    @Operation(
            summary = "Create Terms REST API",
            description = "REST API to create new terms"
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
    public ResponseDTO<TermsResponseDto> create(@Valid @RequestBody TermsRequestDto request) {
        logger.info("TermsController - create started");
        logger.debug("TermsController - request: {}", request.toString());
        TermsResponseDto termsResponseDto = termsService.create(request);
        logger.info("TermsController - create finished");
        return ok(termsResponseDto);
    }

    @Operation(
            summary = "Get Terms REST API",
            description = "REST API to get all terms"
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
    @Retry(name = "getAllRetry")
    @GetMapping
    public ResponseDTO<List<TermsResponseDto>> getAll() {
        logger.info("TermsController - getAll started");
        List<TermsResponseDto> responseDtos = termsService.getAll();
        logger.info("TermsController - getAll finished");
        return ok(responseDtos);
    }

    @Operation(
            summary = "Get Terms REST API",
            description = "REST API to get terms"
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
    @Retry(name = "getRetry")
    @GetMapping("/{termsId}")
    public ResponseDTO<TermsResponseDto> get(@PathVariable Long termsId) {
        logger.info("TermsController - get started");
        logger.debug("TermsController - termsId: {}", termsId);
        TermsResponseDto termsResponseDto = termsService.get(termsId);
        logger.info("TermsController - get finished");
        return ok(termsResponseDto);
    }

    @Operation(
            summary = "Update Terms REST API",
            description = "REST API to update terms"
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
    @PutMapping("/{termsId}")
    public ResponseDTO<TermsResponseDto> update(@PathVariable Long termsId, @Valid @RequestBody TermsRequestDto request) {
        logger.info("TermsController - update started");
        logger.debug("TermsController - termsId: {}", termsId);
        logger.debug("TermsController - request: {}", request.toString());
        TermsResponseDto responseDto = termsService.update(termsId, request);
        logger.info("TermsController - update finished");
        return ok(responseDto);
    }

    @Operation(
            summary = "Delete Terms REST API",
            description = "REST API to delete terms"
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
    @DeleteMapping("/{termsId}")
    public ResponseDTO<TermsResponseDto> delete(@PathVariable Long termsId) {
        logger.info("TermsController - delete started");
        logger.debug("TermsController - termsId: {}", termsId);
        termsService.delete(termsId);
        logger.info("TermsController - delete finished");
        return ok();
    }
}
