package com.example.repayment_server.controller;

import com.example.repayment_server.dto.*;
import com.example.repayment_server.service.IRepaymentService;
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

import java.util.List;

import static com.example.repayment_server.dto.ResponseDTO.ok;

@Tag(
        name = "CRUD REST APIs for Repayment",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE repayment details"
)
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RepaymentController {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentController.class);
    private final IRepaymentService repaymentService;

    @Operation(
            summary = "Create Repayment REST API",
            description = "REST API to create repayment"
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
    @RateLimiter(name = "createRepaymentRateLimiter")
    @PostMapping("/{applicationId}")
    public ResponseDTO<RepaymentResponseDto> createRepayment(
            @PathVariable Long applicationId,
            @Valid @RequestBody RepaymentRequestDto repaymentRequestDto) {
        logger.info("RepaymentController - createRepayment invoked");
        logger.debug("RepaymentController - applicationId: {}", applicationId);
        logger.debug("RepaymentController - repaymentRequestDto: {}", repaymentRequestDto.toString());
        RepaymentResponseDto responseDto = repaymentService.create(applicationId, repaymentRequestDto);
        return ok(responseDto);
    }

    @Operation(
            summary = "Get Repayments REST API",
            description = "REST API to get repayments"
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
    @RateLimiter(name = "getRepaymentsRateLimiter")
    @GetMapping("/{applicationId}")
    public ResponseDTO<List<RepaymentListResponseDto>> getRepayments(
            @PathVariable Long applicationId) {
        logger.info("RepaymentController - getRepayments invoked");
        logger.debug("RepaymentController - applicationId: {}", applicationId);
        List<RepaymentListResponseDto> responseDtos = repaymentService.get(applicationId);
        return ok(responseDtos);
    }

    @Operation(
            summary = "Update Repayment REST API",
            description = "REST API to update repayment"
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
    @RateLimiter(name = "updateRepaymentRateLimiter")
    @PutMapping("/{repaymentId}")
    public ResponseDTO<RepaymentUpdateResponseDto> updateRepayment(
            @PathVariable Long repaymentId,
            @Valid @RequestBody RepaymentRequestDto repaymentRequestDto) {
        logger.info("RepaymentController - updateRepayment invoked");
        logger.debug("RepaymentController - repaymentId: {}", repaymentId);
        logger.debug("RepaymentController - repaymentRequestDto: {}", repaymentRequestDto.toString());
        RepaymentUpdateResponseDto responseDto = repaymentService.update(repaymentId, repaymentRequestDto);
        return ok(responseDto);
    }

    @Operation(
            summary = "Celete Repayment REST API",
            description = "REST API to delete repayment"
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
    @RateLimiter(name = "deleteRepaymentRateLimiter")
    @DeleteMapping("/{repaymentId}")
    public ResponseDTO<Void> deleteRepayment(
            @PathVariable Long repaymentId) {
        logger.info("RepaymentController - createRepayment invoked");
        logger.debug("RepaymentController - deleteRepayment: {}", repaymentId);
        repaymentService.delete(repaymentId);
        return ok();
    }
}

