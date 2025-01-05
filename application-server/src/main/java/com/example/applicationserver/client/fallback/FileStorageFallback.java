package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.FileStorageClient;
import com.example.applicationserver.client.dto.FileResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FileStorageFallback implements FileStorageClient {
    @Override
    public ResponseDTO<Void> upload(Long applicationId, MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<Resource> download(Long applicationId, String fileName) {
        return null;
    }

    @Override
    public ResponseDTO<List<FileResponseDto>> getFilesInfo(Long applicationId) {
        return null;
    }

    @Override
    public ResponseDTO<Void> deleteAll(Long applicationId) {
        return null;
    }
}
