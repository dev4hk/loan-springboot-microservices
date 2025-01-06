package com.example.balance_server.controller;

import com.example.balance_server.dto.*;
import com.example.balance_server.service.IBalanceService;
import io.github.resilience4j.retry.annotation.Retry;
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

@Tag(
        name = "CRUD REST APIs for Balance",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE balance details"
)
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BalanceController {

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
    @PostMapping("/{applicationId}")
    public ResponseDTO<BalanceResponseDto> create(@PathVariable Long applicationId, @Valid @RequestBody BalanceRequestDto request) {
        BalanceResponseDto response = balanceService.create(applicationId, request);
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
    @Retry(name = "get")
    @GetMapping("/{applicationId}")
    public ResponseDTO<BalanceResponseDto> get(@PathVariable Long applicationId) {
        BalanceResponseDto response = balanceService.get(applicationId);
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
    @PutMapping("/{applicationId}")
    public ResponseDTO<BalanceResponseDto> update(@PathVariable Long applicationId, @Valid @RequestBody BalanceUpdateRequestDto request) {
        BalanceResponseDto response = balanceService.update(applicationId, request);
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
    @PutMapping("/{applicationId}/repayment")
    public ResponseDTO<BalanceResponseDto> repaymentUpdate(@PathVariable Long applicationId, @Valid @RequestBody BalanceRepaymentRequestDto request) {
        BalanceResponseDto response = balanceService.repaymentUpdate(applicationId, request);
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
    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Void> delete(@PathVariable Long applicationId) {
        balanceService.delete(applicationId);
        return ResponseDTO.ok();
    }
}
