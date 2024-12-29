package com.example.filestorageserver.controller;

import com.example.filestorageserver.dto.FileResponseDto;
import com.example.filestorageserver.dto.ResponseDTO;
import com.example.filestorageserver.service.IFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;

import static com.example.filestorageserver.dto.ResponseDTO.ok;

@RequiredArgsConstructor
@RequestMapping("/files")
@RestController
public class FileStorageController {

    private final IFileStorageService fileStorageService;

    @PostMapping("/{applicationId}")
    public ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file) {
        fileStorageService.save(applicationId, file);
        return ok();
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<Resource> download(@PathVariable Long applicationId, @RequestParam(value = "fileName") String fileName) {
        Resource file = fileStorageService.load(applicationId, fileName);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

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

    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Void> deleteAll(@PathVariable Long applicationId) {
        fileStorageService.deleteAll(applicationId);
        return ok();
    }

}
