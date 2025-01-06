package com.example.applicationserver.service.impl;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.FileStorageClient;
import com.example.applicationserver.client.JudgementClient;
import com.example.applicationserver.client.TermsClient;
import com.example.applicationserver.client.dto.*;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.GrantAmountDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.entity.Application;
import com.example.applicationserver.exception.BaseException;
import com.example.applicationserver.mapper.ApplicationMapper;
import com.example.applicationserver.repository.ApplicationRepository;
import com.example.applicationserver.service.IApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements IApplicationService {

    private final ApplicationRepository applicationRepository;
    private final TermsClient termClient;
    private final AcceptTermsClient acceptTermsClient;
    private final FileStorageClient fileStorageClient;
    private final JudgementClient judgementClient;

    @Override
    public ApplicationResponseDto create(ApplicationRequestDto request) {
        Application application = ApplicationMapper.mapToApplication(request);
        application.setAppliedAt(LocalDateTime.now());
        Application created = applicationRepository.save(application);
        return ApplicationMapper.mapToApplicationResponseDto(created);
    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponseDto get(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exists", HttpStatus.NOT_FOUND));
        return ApplicationMapper.mapToApplicationResponseDto(application);
    }

    @Override
    public ApplicationResponseDto update(Long applicationId, ApplicationRequestDto request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exists", HttpStatus.NOT_FOUND));
        application.setFirstname(request.getFirstname());
        application.setLastname(request.getLastname());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        return ApplicationMapper.mapToApplicationResponseDto(application);
    }

    @Override
    public void delete(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exists", HttpStatus.NOT_FOUND));
        application.setIsDeleted(true);
    }

    @Override
    public void acceptTerms(Long applicationId, AcceptTermsRequestDto request) {
        get(applicationId);
        ResponseDTO<List<TermsResponseDto>> termsResponse = termClient.getAll();
        if (termsResponse.getResult().code.equals(ResultType.SYSTEM_ERROR.getCode())) {
            throw new BaseException(ResultType.SYSTEM_ERROR, termsResponse.getResult().desc, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<TermsResponseDto> terms = termsResponse.getData();

        if (terms.isEmpty()) {
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, "Terms server error", HttpStatus.NOT_FOUND);
        }

        List<Long> requestTermsIds = request.getTermsIds();

        if (terms.size() != requestTermsIds.size()) {
            throw new BaseException(ResultType.BAD_REQUEST, "Terms size does not match", HttpStatus.BAD_REQUEST);
        }

        List<Long> termsIds = terms.stream().map(TermsResponseDto::getTermsId).toList();

        if (!termsIds.containsAll(requestTermsIds)) {
            throw new BaseException(ResultType.BAD_REQUEST, "Terms does not match", HttpStatus.BAD_REQUEST);
        }

        AcceptTermsRequestDto.builder()
                .termsIds(requestTermsIds)
                .applicationId(applicationId)
                .build();

        ResponseDTO<List<AcceptTermsResponseDto>> acceptTermsResponse = acceptTermsClient.create(request);
        if (acceptTermsResponse.getResult().code.equals(ResultType.SYSTEM_ERROR.getCode())) {
            throw new BaseException(ResultType.SYSTEM_ERROR, acceptTermsResponse.getResult().desc, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void uploadFile(Long applicationId, MultipartFile file) {
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exists", HttpStatus.NOT_FOUND);
        }
        ResponseDTO<Void> fileStorageResponse = fileStorageClient.upload(applicationId, file);
        if (fileStorageResponse.getResult().code.equals(ResultType.SYSTEM_ERROR.getCode())) {
            throw new BaseException(ResultType.SYSTEM_ERROR, fileStorageResponse.getResult().desc, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Resource downloadFile(Long applicationId, String fileName) {
        ResponseEntity<Resource> fileStorageResponse = fileStorageClient.download(applicationId, fileName);
        if (fileStorageResponse.getStatusCode().is5xxServerError()) {
            throw new BaseException(ResultType.SYSTEM_ERROR, "Error downloading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return fileStorageResponse.getBody();
    }

    @Override
    public List<FileResponseDto> loadAllFiles(Long applicationId) {
        ResponseDTO<List<FileResponseDto>> fileStorageResponse = fileStorageClient.getFilesInfo(applicationId);
        if (fileStorageResponse.getResult().code.equals(ResultType.SYSTEM_ERROR.getCode())) {
            throw new BaseException(ResultType.SYSTEM_ERROR, fileStorageResponse.getResult().desc, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return fileStorageResponse.getData();
    }

    @Override
    public void deleteAllFiles(Long applicationId) {
        ResponseDTO<Void> fileStorageResponse = fileStorageClient.deleteAll(applicationId);
        if (fileStorageResponse.getResult().code.equals(ResultType.SYSTEM_ERROR.getCode())) {
            throw new BaseException(ResultType.SYSTEM_ERROR, fileStorageResponse.getResult().desc, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateGrant(Long applicationId, GrantAmountDto grantAmountDto) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exists", HttpStatus.NOT_FOUND));
        application.setApprovalAmount(grantAmountDto.getApprovalAmount());
    }

    @Override
    public ApplicationResponseDto contract(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exists", HttpStatus.NOT_FOUND));

        ResponseDTO<JudgementResponseDto> judgementResponse = judgementClient.getJudgmentOfApplication(applicationId);
        if (judgementResponse.getResult().code.equals(ResultType.SYSTEM_ERROR.getCode())) {
            throw new BaseException(ResultType.SYSTEM_ERROR, judgementResponse.getResult().desc, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (judgementResponse.getData() == null) {
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, "Judgement does not exist", HttpStatus.NOT_FOUND);
        }

        if (application.getApprovalAmount() == null || application.getApprovalAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new BaseException(ResultType.BAD_REQUEST, "Approval amount does not exist", HttpStatus.BAD_REQUEST);
        }

        application.setContractedAt(LocalDateTime.now());
        return ApplicationMapper.mapToApplicationResponseDto(application);

    }

    private boolean isPresentApplication(Long applicationId) {

        return applicationRepository.existsById(applicationId);
    }
}
