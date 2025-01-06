package com.example.filestorageserver.controller;

import com.example.filestorageserver.constants.ResultType;
import com.example.filestorageserver.dto.FileResponseDto;
import com.example.filestorageserver.dto.ResponseDTO;
import com.example.filestorageserver.exception.BaseException;
import com.example.filestorageserver.service.IFileStorageService;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    @PostMapping(value = "/{applicationId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file) {
        fileStorageService.save(applicationId, file);
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
    @Retry(name = "download", fallbackMethod = "downloadFallback")
    @GetMapping("/{applicationId}")
    public ResponseEntity<Resource> download(@PathVariable Long applicationId, @RequestParam(value = "fileName") String fileName) {
        Resource file = fileStorageService.load(applicationId, fileName);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    public ResponseEntity<Resource> downloadFallback(Long applicationId, String fileName, Throwable throwable) {
       throw new BaseException(ResultType.SYSTEM_ERROR, "File storage server timeout", HttpStatus.INTERNAL_SERVER_ERROR);
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
    @Retry(name = "getFilesInfo", fallbackMethod = "getFilesInfoFallback")
    @GetMapping("/{applicationId}/info")
    public ResponseDTO<List<FileResponseDto>> getFilesInfo(@PathVariable Long applicationId) {
        List<FileResponseDto> filesInfo = fileStorageService.loadAll(applicationId).map(path -> {
            String fileName = path.getFileName().toString();
            return FileResponseDto.builder()
                    .name(fileName)
                    .url(MvcUriComponentsBuilder.fromMethodName(FileStorageController.class, "download", applicationId, fileName).build().toString())
                    .build();
        }).toList();
        return ok(filesInfo);
    }

    public ResponseDTO<List<FileResponseDto>> getFilesInfoFallback(Long applicationId, Throwable throwable) {
        throw new BaseException(ResultType.SYSTEM_ERROR, "File storage server timeout", HttpStatus.INTERNAL_SERVER_ERROR);
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
    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Void> deleteAll(@PathVariable Long applicationId) {
        fileStorageService.deleteAll(applicationId);
        return ok();
    }

}
