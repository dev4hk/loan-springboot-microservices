package com.example.applicationserver.service;

import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.FileResponseDto;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.GrantAmountDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    void updateGrant(Long applicationId, GrantAmountDto grantAmountDto);

    ApplicationResponseDto contract(Long applicationId);
}
