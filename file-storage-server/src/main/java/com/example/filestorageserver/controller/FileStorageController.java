package com.example.filestorageserver.controller;

import com.example.filestorageserver.dto.FileResponseDto;
import com.example.filestorageserver.dto.ResponseDTO;
import com.example.filestorageserver.service.IFileStorageService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;

import static com.example.filestorageserver.dto.ResponseDTO.ok;

@Tag(
        name = "CRUD REST APIs for File Storage",
        description = "REST APIs to upload, download, fetch file info, delete files"
)
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class FileStorageController {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageController.class);
    private final IFileStorageService fileStorageService;

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
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @RateLimiter(name = "uploadRateLimiter")
    @PostMapping(value = "/{applicationId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file) {
        logger.info("FileStorageController - upload started");
        logger.debug("FileStorageController - applicationId: {}", applicationId);
        logger.debug("FileStorageController - originalFilename: {}", file.getOriginalFilename());
        fileStorageService.save(applicationId, file);
        logger.info("FileStorageController - upload finished");
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
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @RateLimiter(name = "downloadRateLimiter")
    @Retry(name = "download")
    @GetMapping("/{applicationId}")
    public ResponseEntity<Resource> download(@PathVariable Long applicationId, @RequestParam(value = "fileName") String fileName) {
        logger.info("FileStorageController - download started");
        logger.debug("FileStorageController - applicationId: {}", applicationId);
        logger.debug("FileStorageController - fileName: {}", fileName);
        Resource file = fileStorageService.load(applicationId, fileName);
        logger.info("FileStorageController - download finished");
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @Operation(
            summary = "Fetch file info REST API",
            description = "REST API to fetch file info"
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
    @RateLimiter(name = "getFilesInfoRateLimiter")
    @Retry(name = "getFilesInfo")
    @GetMapping("/{applicationId}/info")
    public ResponseDTO<List<FileResponseDto>> getFilesInfo(@PathVariable Long applicationId) {
        logger.info("FileStorageController - getFileInfo started");
        logger.debug("FileStorageController - applicationId: {}", applicationId);
        List<FileResponseDto> filesInfo = fileStorageService.loadAll(applicationId).map(path -> {
            String fileName = path.getFileName().toString();
            return FileResponseDto.builder()
                    .name(fileName)
                    .url(MvcUriComponentsBuilder.fromMethodName(FileStorageController.class, "download", applicationId, fileName).build().toString())
                    .build();
        }).toList();
        logger.info("FileStorageController - getFileInfo finished");
        return ok(filesInfo);
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
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @RateLimiter(name = "deleteAllRateLimiter")
    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Void> deleteAll(@PathVariable Long applicationId) {
        logger.info("FileStorageController - deleteAll started");
        logger.debug("FileStorageController - applicationId: {}", applicationId);
        fileStorageService.deleteAll(applicationId);
        logger.info("FileStorageController - deleteAll finished");
        return ok();
    }

}
