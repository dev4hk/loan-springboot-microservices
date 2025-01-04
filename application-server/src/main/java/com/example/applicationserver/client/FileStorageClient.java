package com.example.applicationserver.client;

import com.example.applicationserver.client.dto.FileResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(value = "file-storage-server", url = "${client.file-storage.url}")
public interface FileStorageClient {

    @PostMapping(value = "/files/{applicationId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file);

    @GetMapping("/files/{applicationId}")
    ResponseEntity<Resource> download(@PathVariable Long applicationId, @RequestParam(value = "fileName") String fileName);

    @GetMapping("/files/{applicationId}/info")
    ResponseDTO<List<FileResponseDto>> getFilesInfo(@PathVariable Long applicationId);

    @DeleteMapping("/files/{applicationId}")
    ResponseDTO<Void> deleteAll(@PathVariable Long applicationId);
}
