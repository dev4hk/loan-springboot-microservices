package com.example.filestorageserver.service.impl;

import com.example.filestorageserver.constants.ResultType;
import com.example.filestorageserver.exception.BaseException;
import com.example.filestorageserver.service.IFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class FileStorageServiceImpl implements IFileStorageService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Override
    public void save(Long applicationId, MultipartFile file) {
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
            System.out.println(e.getLocalizedMessage());
            throw new BaseException(ResultType.SYSTEM_ERROR, e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Resource load(Long applicationId, String fileName) {
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            Path file = Paths.get(applicationPath).resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new BaseException(ResultType.RESOURCE_NOT_FOUND, "File does not exist", HttpStatus.NOT_FOUND);
            }

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR, e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public Stream<Path> loadAll(Long applicationId) {
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            return Files.walk(Paths.get(applicationPath), 1).filter(path -> !path.equals(Paths.get(applicationPath)));
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR, e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteAll(Long applicationId) {
        String applicationPath = uploadPath.concat("/" + applicationId);
        FileSystemUtils.deleteRecursively(Paths.get(applicationPath).toFile());
    }

}
