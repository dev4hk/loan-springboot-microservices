package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.FileStorageClient;
import com.example.applicationserver.client.dto.FileResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileStorageClientFallbackFactory implements FallbackFactory<FileStorageClient> {
    @Override
    public FileStorageClient create(Throwable cause) {
        return new FileStorageClient() {
            @Override
            public ResponseDTO<List<FileResponseDto>> getFilesInfo(Long applicationId) {
                return null;
            }
        };
    }
}
