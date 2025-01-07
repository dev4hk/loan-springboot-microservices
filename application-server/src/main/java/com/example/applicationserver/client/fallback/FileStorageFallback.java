package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.FileStorageClient;
import com.example.applicationserver.client.dto.FileResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.dto.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FileStorageFallback implements FileStorageClient {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageFallback.class);

    @Override
    public ResponseDTO<Void> upload(Long applicationId, MultipartFile file) {
        logger.error("FileStorageFallback - upload invoked for applicationId: {}", applicationId);

        ResultObject resultObject = ResultObject.builder()
                .code(ResultType.SYSTEM_ERROR.getCode())
                .desc("Error uploading file")
                .build();

        return new ResponseDTO<>(resultObject, null);
    }

    @Override
    public ResponseEntity<Resource> download(Long applicationId, String fileName) {
        logger.error("FileStorageFallback - download invoked for applicationId: {}", applicationId);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseDTO<List<FileResponseDto>> getFilesInfo(Long applicationId) {
        logger.error("FileStorageFallback - getFilesInfo invoked for applicationId: {}", applicationId);

        ResultObject resultObject = ResultObject.builder()
                .code(ResultType.SYSTEM_ERROR.getCode())
                .desc("Error fetching files info")
                .build();

        return new ResponseDTO<>(resultObject, null);
    }

    @Override
    public ResponseDTO<Void> deleteAll(Long applicationId) {
        logger.error("FileStorageFallback - deleteAll invoked for applicationId: {}", applicationId);

        ResultObject resultObject = ResultObject.builder()
                .code(ResultType.SYSTEM_ERROR.getCode())
                .desc("Error deleting files")
                .build();

        return new ResponseDTO<>(resultObject, null);
    }
}
