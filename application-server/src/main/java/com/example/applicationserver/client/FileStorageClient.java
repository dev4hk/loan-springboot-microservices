package com.example.applicationserver.client;

import com.example.applicationserver.client.dto.FileResponseDto;
import com.example.applicationserver.client.fallback.FileStorageClientFallbackFactory;
import com.example.applicationserver.config.FeignConfig;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "file-storage-server", fallbackFactory = FileStorageClientFallbackFactory.class, configuration = FeignConfig.class)
public interface FileStorageClient {
    @GetMapping("/{applicationId}/info")
    public ResponseDTO<List<FileResponseDto>> getFilesInfo(@PathVariable Long applicationId);
}
