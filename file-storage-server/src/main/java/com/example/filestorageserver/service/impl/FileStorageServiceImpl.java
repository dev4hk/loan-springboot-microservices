package com.example.filestorageserver.service.impl;

import com.example.filestorageserver.client.ApplicationClient;
import com.example.filestorageserver.client.dto.ApplicationResponseDto;
import com.example.filestorageserver.constants.ResultType;
import com.example.filestorageserver.dto.ResponseDTO;
import com.example.filestorageserver.exception.BaseException;
import com.example.filestorageserver.service.IFileStorageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class FileStorageServiceImpl implements IFileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final ApplicationClient applicationClient;

    @Override
    public void save(Long applicationId, MultipartFile file) {
        logger.info("FileStorageServiceImpl - upload invoked");
        if (!checkIfApplicationPresent(applicationId)) {
            logger.error("FileStorageServiceImpl - Application does not exist");
            throw new BaseException(ResultType.BAD_REQUEST, "Application does not exist", HttpStatus.BAD_REQUEST);
        }
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            Path directory = Path.of(applicationPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            Files.copy(
                    file.getInputStream(),
                    Paths.get(applicationPath).resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            logger.error("FileStorageServiceImpl - {}", e.getLocalizedMessage());
            throw new BaseException(ResultType.SYSTEM_ERROR, e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Resource load(Long applicationId, String fileName) {
        logger.info("FileStorageServiceImpl - download invoked");
        if (!checkIfApplicationPresent(applicationId)) {
            logger.error("FileStorageServiceImpl - Application does not exist");
            throw new BaseException(ResultType.BAD_REQUEST, "Application does not exist", HttpStatus.BAD_REQUEST);
        }
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            Path file = Paths.get(applicationPath).resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                logger.error("FileStorageServiceImpl - File does not exist");
                throw new BaseException(ResultType.RESOURCE_NOT_FOUND, "File does not exist", HttpStatus.NOT_FOUND);
            }

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            logger.error("FileStorageServiceImpl - Integnal server error");
            throw new BaseException(ResultType.SYSTEM_ERROR, e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public Stream<Path> loadAll(Long applicationId) {
        logger.info("FileStorageServiceImpl - getFileInfo invoked");
        if (!checkIfApplicationPresent(applicationId)) {
            logger.error("FileStorageServiceImpl - Application does not exist");
            throw new BaseException(ResultType.BAD_REQUEST, "Application does not exist", HttpStatus.BAD_REQUEST);
        }
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            return Files.walk(Paths.get(applicationPath), 1).filter(path -> !path.equals(Paths.get(applicationPath)));
        } catch (Exception e) {
            logger.error("FileStorageServiceImpl - {}", e.getLocalizedMessage());
            throw new BaseException(ResultType.SYSTEM_ERROR, e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteAll(Long applicationId) {
        logger.info("FileStorageServiceImpl - deleteAll invoked");
        if (!checkIfApplicationPresent(applicationId)) {
            logger.error("FileStorageServiceImpl - Application does not exist");
            throw new BaseException(ResultType.BAD_REQUEST, "Application does not exist", HttpStatus.BAD_REQUEST);
        }
        String applicationPath = uploadPath.concat("/" + applicationId);
        FileSystemUtils.deleteRecursively(Paths.get(applicationPath).toFile());
    }

    @Override
    public void deleteFile(Long applicationId, String fileName) {
        logger.info("FileStorageServiceImpl - deleteFile invoked");

        if (!checkIfApplicationPresent(applicationId)) {
            logger.error("FileStorageServiceImpl - Application does not exist");
            throw new BaseException(ResultType.BAD_REQUEST, "Application does not exist", HttpStatus.BAD_REQUEST);
        }

        String filePath = uploadPath + "/" + applicationId + "/" + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            logger.error("FileStorageServiceImpl - File not found: {}", fileName);
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, "File not found", HttpStatus.NOT_FOUND);
        }

        if (file.delete()) {
            logger.info("FileStorageServiceImpl - File successfully deleted: {}", fileName);
        } else {
            logger.error("FileStorageServiceImpl - Failed to delete file: {}", fileName);
            throw new BaseException(ResultType.SYSTEM_ERROR, "Failed to delete file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public boolean checkIfApplicationPresent(Long applicationId) {
        logger.info("FileStorageServiceImpl - checkIfApplicationPresent invoked");
        ResponseDTO<ApplicationResponseDto> responseDto = applicationClient.get(applicationId);
        return responseDto.getData() != null;
    }

}
