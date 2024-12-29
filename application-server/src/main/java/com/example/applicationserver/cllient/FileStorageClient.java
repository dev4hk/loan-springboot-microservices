package com.example.applicationserver.cllient;

import com.example.applicationserver.cllient.dto.FileResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "file-storage-server", url = "http://localhost:8084")
public interface FileStorageClient {

    @PostMapping("/files/{applicationId}")
    ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file);

    @GetMapping("/files/{applicationId}")
    ResponseEntity<Resource> download(@PathVariable Long applicationId, @RequestParam(value = "fileName") String fileName);

    @GetMapping("/{applicationId}/info")
    ResponseDTO<List<FileResponseDto>> getFilesInfo(@PathVariable Long applicationId);

    @DeleteMapping("/{applicationId}")
    ResponseDTO<Void> deleteAll(@PathVariable Long applicationId);
}
