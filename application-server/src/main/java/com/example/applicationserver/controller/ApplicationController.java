package com.example.applicationserver.controller;

import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.CombinedApplicationRequestDto;
import com.example.applicationserver.constants.CommunicationStatus;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.GrantAmountDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.service.IApplicationService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.applicationserver.dto.ResponseDTO.ok;

@Tag(
        name = "Application",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE application details"
)
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
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
    @RateLimiter(name = "createRateLimiter")
    @PostMapping
    public ResponseDTO<ApplicationResponseDto> create(@Valid @RequestBody CombinedApplicationRequestDto requestDto) {
        logger.info("ApplicationController - create started");
        ApplicationResponseDto applicationResponseDto = applicationService.create(requestDto.getApplicationRequestDto(), requestDto.getAcceptTermsRequestDto());
        logger.info("ApplicationController - create finished");
        return ok(applicationResponseDto);
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
    @RateLimiter(name = "getRateLimiter")
    @Retry(name = "getRetry")
    @GetMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId) {
        logger.info("ApplicationController - get started");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        ApplicationResponseDto applicationResponseDto = applicationService.get(applicationId);
        logger.info("ApplicationController - get finished");
        return ok(applicationResponseDto);
    }

    @Operation(
            summary = "Get Application REST API By Email",
            description = "REST API to get application by email"
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
    @GetMapping("/email")
    public ResponseDTO<ApplicationResponseDto> getByEmail(@RequestParam("email") String email) {
        logger.info("ApplicationController - get started");
        logger.debug("ApplicationController - applicationID: {}", email);
        ApplicationResponseDto applicationResponseDto = applicationService.getByEmail(email);
        logger.info("ApplicationController - get finished");
        return ok(applicationResponseDto);
    }

    @Operation(
            summary = "Get All Applications REST API",
            description = "REST API to get all applications"
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
    public ResponseDTO<Page<ApplicationResponseDto>> getAll(Pageable pageable) {
        logger.info("ApplicationController - getAll started");
        Page<ApplicationResponseDto> response = applicationService.getAll(pageable);
        logger.info("ApplicationController - getAll finished");
        return ok(response);
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
    @RateLimiter(name = "updateRateLimiter")
    @PutMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> update(@PathVariable Long applicationId, @Valid @RequestBody ApplicationRequestDto request) {
        logger.info("ApplicationController - update started");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        logger.debug("ApplicationController - request: {}", request.toString());
        ApplicationResponseDto update = applicationService.update(applicationId, request);
        logger.info("ApplicationController - update finished");
        return ok(update);
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
    @RateLimiter(name = "deleteRateLimiter")
    @DeleteMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> delete(@PathVariable Long applicationId) {
        logger.info("ApplicationController - delete started");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        applicationService.delete(applicationId);
        logger.info("ApplicationController - delete finished");
        return ok();
    }

    @Operation(
            summary = "Accept terms REST API",
            description = "REST API to accept terms"
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
                    responseCode = "404",
                    description = "HTTP Not Found",
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
    @RateLimiter(name = "acceptTermsRateLimiter")
    @PostMapping("/{applicationId}/terms")
    public ResponseDTO<Void> acceptTerms(@PathVariable Long applicationId, @Valid @RequestBody AcceptTermsRequestDto request) {
        logger.info("ApplicationController - acceptTerms started");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        logger.debug("ApplicationController - request: {}", request.toString());
        applicationService.acceptTerms(applicationId, request);
        logger.info("ApplicationController - acceptTerms finished");
        return ok();
    }

    @Operation(
            summary = "Update grant REST API",
            description = "REST API to update grant"
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
                    responseCode = "404",
                    description = "HTTP Not Found",
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
    @RateLimiter(name = "updateGrantRateLimiter")
    @PutMapping("/{applicationId}/grant")
    public ResponseDTO<Void> updateGrant(@PathVariable Long applicationId, @Valid @RequestBody GrantAmountDto grantAmountDto) {
        logger.info("ApplicationController - updateGrant started");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        logger.debug("ApplicationController - grantAmountDto: {}", grantAmountDto.toString());
        applicationService.updateGrant(applicationId, grantAmountDto);
        logger.info("ApplicationController - updateGrant finished");
        return ok();
    }

    @Operation(
            summary = "Contract REST API",
            description = "REST API to contract"
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
                    responseCode = "404",
                    description = "HTTP Not Found",
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
    @RateLimiter(name = "contractRateLimiter")
    @PutMapping("/{applicationId}/contract")
    public ResponseDTO<ApplicationResponseDto> contract(@PathVariable Long applicationId) {
        logger.info("ApplicationController - contract started");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        ApplicationResponseDto contract = applicationService.contract(applicationId);
        logger.info("ApplicationController - contract finished");
        return ok(contract);
    }

    @Operation(
            summary = "Application Statistics REST API",
            description = "REST API to get application statistics"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Not Found",
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
    @RateLimiter(name = "getStatsRateLimiter")
    @GetMapping("/stats")
    public ResponseDTO<Map<CommunicationStatus, Long>> getStats() {
        logger.info("ApplicationController - getStats started");
        Map<CommunicationStatus, Long> stats = applicationService.getApplicationStatistics();
        logger.info("ApplicationController - getStats finished");
        return ok(stats);
    }

    @Operation(
            summary = "complete REST API",
            description = "REST API to complete application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Not Found",
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
    @RateLimiter(name = "completeLimiter")
    @PutMapping("/{applicationId}/complete")
    public ResponseDTO<Void> complete(@PathVariable Long applicationId) {
        logger.info("ApplicationController - complete started");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        applicationService.complete(applicationId);
        logger.info("ApplicationController - complete finished");
        return ok();
    }

    @GetMapping("/new")
    public ResponseDTO<List<ApplicationResponseDto>> getNewApplications() {
        logger.info("ApplicationController - getNewApplications started");
        List<ApplicationResponseDto> applications = applicationService.getNewApplications();
        logger.info("ApplicationController - getNewApplications finished");
        return ok(applications);
    }

}
