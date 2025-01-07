package com.example.judgement_server.controller;

import com.example.judgement_server.constants.ResultType;
import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;
import com.example.judgement_server.dto.ResponseDTO;
import com.example.judgement_server.exception.BaseException;
import com.example.judgement_server.service.IJudgementService;
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
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.judgement_server.dto.ResponseDTO.ok;

@Tag(
        name = "CRUD REST APIs for Judgement",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE judgement details"
)
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class JudgementController {

    private final IJudgementService judgementService;

    @Operation(
            summary = "Create Judgement REST API",
            description = "REST API to create new Judgement"
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
                    description = "HTTP Status Not Found",
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
    public ResponseDTO<JudgementResponseDto> create(@Valid @RequestBody JudgementRequestDto request) {
        return ok(judgementService.create(request));
    }

    @Operation(
            summary = "Get Judgement REST API",
            description = "REST API to get Judgement"
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
                    description = "HTTP Status Not Found",
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
    @GetMapping("/{judgementId}")
    public ResponseDTO<JudgementResponseDto> get(@PathVariable Long judgementId) {
        return ok(judgementService.get(judgementId));
    }

    @Operation(
            summary = "Get Judgement by application id REST API",
            description = "REST API to get judgement by application id"
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
                    description = "HTTP Status Not Found",
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
    @RateLimiter(name = "getJudgmentOfApplicationRateLimiter")
    @Retry(name = "getJudgmentOfApplicationRetry")
    @GetMapping("/applications/{applicationId}")
    public ResponseDTO<JudgementResponseDto> getJudgmentOfApplication(@PathVariable Long applicationId) {
        return ok(judgementService.getJudgementOfApplication(applicationId));
    }

    @Operation(
            summary = "Update Judgement REST API",
            description = "REST API to update Judgement"
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
                    description = "HTTP Status Not Found",
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
    @PutMapping("/{judgementId}")
    public ResponseDTO<JudgementResponseDto> update(@PathVariable Long judgementId, @Valid @RequestBody JudgementRequestDto request) {
        return ok(judgementService.update(judgementId, request));
    }

    @Operation(
            summary = "Delete Judgement REST API",
            description = "REST API to delete Judgement"
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
                    description = "HTTP Status Not Found",
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
    @DeleteMapping("/{judgementId}")
    public ResponseDTO<Void> delete(@PathVariable Long judgementId) {
        judgementService.delete(judgementId);
        return ok();
    }

    @Operation(
            summary = "Grant Approval Amount REST API",
            description = "REST API to add new Grant Approval Amount"
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
                    description = "HTTP Status Not Found",
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
    @RateLimiter(name = "grantRateLimiter")
    @PatchMapping("/{judgementId}/grants")
    public ResponseDTO<GrantAmountDto> grant(@PathVariable Long judgementId) {
        return ok(judgementService.grant(judgementId));
    }

}
