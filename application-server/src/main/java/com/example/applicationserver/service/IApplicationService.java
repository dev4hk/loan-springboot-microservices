package com.example.applicationserver.service;

import com.example.applicationserver.cllient.dto.AcceptTermsRequestDto;
import com.example.applicationserver.cllient.dto.FileResponseDto;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface IApplicationService {
    ApplicationResponseDto create(ApplicationRequestDto request);

    ApplicationResponseDto get(Long findId);

    ApplicationResponseDto update(Long applicationId, ApplicationRequestDto request);

    void delete(Long applicationId);

    void acceptTerms(Long applicationId, AcceptTermsRequestDto request);

    void uploadFile(Long applicationId, MultipartFile file);

    Resource downloadFile(Long applicationId, String fileName);

    List<FileResponseDto> loadAllFiles(Long applicationId);

    void deleteAllFiles(Long applicationId);
}
