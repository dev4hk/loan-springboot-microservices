package com.example.balance_server.controller;

import com.example.balance_server.dto.*;
import com.example.balance_server.service.IBalanceService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Balance",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE balance details"
)
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BalanceController {

    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);

    private final IBalanceService balanceService;

    @Operation(
            summary = "Create Balance REST API",
            description = "REST API to create new balance"
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
    @PostMapping("/{applicationId}")
    public ResponseDTO<BalanceResponseDto> create(@PathVariable Long applicationId, @Valid @RequestBody BalanceRequestDto request) {
        logger.info("BalanceController - create started");
        logger.debug("BalanceController - applicationId: {}", applicationId);
        logger.debug("BalanceController - request: {}", request.toString());
        BalanceResponseDto response = balanceService.create(applicationId, request);
        logger.info("BalanceController - create finished");
        return ResponseDTO.ok(response);
    }

    @Operation(
            summary = "Get Balance REST API",
            description = "REST API to get balance"
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
    @Retry(name = "getRetry")
    @RateLimiter(name = "getRateLimiter")
    @GetMapping("/{applicationId}")
    public ResponseDTO<BalanceResponseDto> get(@PathVariable Long applicationId) {
        logger.info("BalanceController - get started");
        logger.debug("BalanceController - applicationId: {}", applicationId);
        BalanceResponseDto response = balanceService.get(applicationId);
        logger.info("BalanceController - get finished");
        return ResponseDTO.ok(response);
    }

    @Operation(
            summary = "Update Balance REST API",
            description = "REST API to update balance"
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
    @PutMapping("/{applicationId}")
    public ResponseDTO<BalanceResponseDto> update(@PathVariable Long applicationId, @Valid @RequestBody BalanceUpdateRequestDto request) {
        logger.info("BalanceController - update started");
        logger.debug("BalanceController - applicationId: {}", applicationId);
        logger.debug("BalanceController - request: {}", request.toString());
        BalanceResponseDto response = balanceService.update(applicationId, request);
        logger.info("BalanceController - update finished");
        return ResponseDTO.ok(response);
    }

    @Operation(
            summary = "Repayment REST API",
            description = "REST API to make repayment"
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
    @RateLimiter(name = "repaymentUpdateRateLimiter")
    @PutMapping("/{applicationId}/repayment")
    public ResponseDTO<List<BalanceResponseDto>> repaymentUpdate(@PathVariable Long applicationId, @RequestBody @NotEmpty List<@Valid BalanceRepaymentRequestDto> request) {
        logger.info("BalanceController - repaymentUpdate started");
        logger.debug("BalanceController - applicationId: {}", applicationId);
        logger.debug("BalanceController - request: {}", request.toString());
        List<BalanceResponseDto> response = balanceService.repaymentUpdate(applicationId, request);
        logger.info("BalanceController - repaymentUpdate finished");
        return ResponseDTO.ok(response);
    }

    @Operation(
            summary = "Delete Balance REST API",
            description = "REST API to delete balance"
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
    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Void> delete(@PathVariable Long applicationId) {
        logger.info("BalanceController - delete started");
        logger.debug("BalanceController - applicationId: {}", applicationId);
        balanceService.delete(applicationId);
        logger.info("BalanceController - delete finished");
        return ResponseDTO.ok();
    }
}
