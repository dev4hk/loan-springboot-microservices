package com.example.filestorageserver.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IFileStorageService {
    void save(Long applicationId, MultipartFile file);
    Resource load(Long applicationId, String fileName);
    Stream<Path> loadAll(Long applicationId);
    void deleteAll(Long applicationId);
}
