package com.example.applicationserver.controller;

import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.FileResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.GrantAmountDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.exception.BaseException;
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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.applicationserver.dto.ResponseDTO.ok;

@Tag(
        name = "CRUD REST APIs for Application",
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
    public ResponseDTO<ApplicationResponseDto> create(@Valid @RequestBody ApplicationRequestDto request) {
        logger.info("ApplicationController - create invoked");
        logger.debug("ApplicationController - request: {}", request.toString());
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
    @RateLimiter(name = "getRateLimiter")
    @Retry(name = "getRetry")
    @GetMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId) {
        logger.info("ApplicationController - get invoked");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
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
    @RateLimiter(name = "updateRateLimiter")
    @PutMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> update(@PathVariable Long applicationId, @Valid @RequestBody ApplicationRequestDto request) {
        logger.info("ApplicationController - update invoked");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        logger.debug("ApplicationController - request: {}", request.toString());
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
    @RateLimiter(name = "deleteRateLimiter")
    @DeleteMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> delete(@PathVariable Long applicationId) {
        logger.info("ApplicationController - delete invoked");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        applicationService.delete(applicationId);
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
        logger.info("ApplicationController - acceptTerms invoked");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        logger.debug("ApplicationController - request: {}", request.toString());
        applicationService.acceptTerms(applicationId, request);
        return ok();
    }

    @Operation(
            summary = "Upload file REST API",
            description = "REST API to upload file"
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
    @RateLimiter(name = "uploadRateLimiter")
    @PostMapping(value = "/{applicationId}/files", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file) {
        logger.info("ApplicationController - upload invoked");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        applicationService.uploadFile(applicationId, file);
        return ok();
    }

    @Operation(
            summary = "Download file REST API",
            description = "REST API to download file"
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
    @RateLimiter(name = "downloadRateLimiter")
    @GetMapping("/{applicationId}/files")
    public ResponseEntity<Resource> download(@PathVariable Long applicationId, @RequestParam(value = "fileName") String fileName) {
        logger.info("ApplicationController - download invoked");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        logger.debug("ApplicationController - fileName: {}", fileName);
        return ResponseEntity.ok(applicationService.downloadFile(applicationId, fileName));
    }

    @Operation(
            summary = "Fetch files info REST API",
            description = "REST API to fetch files info"
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
    @RateLimiter(name = "getFilesInfoRateLimiter")
    @GetMapping("/{applicationId}/files/info")
    public ResponseDTO<List<FileResponseDto>> getFilesInfo(@PathVariable Long applicationId) {
        logger.info("ApplicationController - getFilesInfo invoked");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        return ok(applicationService.loadAllFiles(applicationId));
    }

    @Operation(
            summary = "Delete files REST API",
            description = "REST API to delete files"
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
    @RateLimiter(name = "deleteAllFilesRateLimiter")
    @DeleteMapping("/{applicationId}/files")
    public ResponseDTO<Void> deleteAllFiles(@PathVariable Long applicationId) {
        logger.info("ApplicationController - deleteAllFiles invoked");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        applicationService.deleteAllFiles(applicationId);
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
        logger.info("ApplicationController - updateGrant invoked");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        logger.debug("ApplicationController - grantAmountDto: {}", grantAmountDto.toString());
        applicationService.updateGrant(applicationId, grantAmountDto);
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
        logger.info("ApplicationController - contract invoked");
        logger.debug("ApplicationController - applicationID: {}", applicationId);
        return ok(applicationService.contract(applicationId));
    }

}
