package com.example.applicationserver.controller;

import com.example.applicationserver.cllient.dto.AcceptTermsRequestDto;
import com.example.applicationserver.cllient.dto.FileResponseDto;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.service.IApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
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
@RequestMapping("/applications")
@RestController
public class ApplicationController {

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
    @PostMapping
    public ResponseDTO<ApplicationResponseDto> create(@Valid @RequestBody ApplicationRequestDto request) {
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
    @GetMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId) {
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
    @PutMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> update(@PathVariable Long applicationId, @Valid @RequestBody ApplicationRequestDto request) {
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
    @DeleteMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> delete(@PathVariable Long applicationId) {
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
    @PostMapping("/{applicationId}/terms")
    public ResponseDTO<Void> acceptTerms(@PathVariable Long applicationId, @RequestBody AcceptTermsRequestDto request) {
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
    @PostMapping("/{applicationId}/files")
    public ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file) {
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
    @GetMapping("/{applicationId}/files")
    public ResponseDTO<Resource> download(@PathVariable Long applicationId, @RequestParam(value = "fileName") String fileName) {
        return ok(applicationService.downloadFile(applicationId, fileName));
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
    @GetMapping("/{applicationId}/files/info")
    public ResponseDTO<List<FileResponseDto>> getFilesInfo(@PathVariable Long applicationId) {
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
    @DeleteMapping("{applicationId}/files")
    public ResponseDTO<Void> deleteAllFiles(@PathVariable Long applicationId) {
        applicationService.deleteAllFiles(applicationId);
        return ok();
    }

}
