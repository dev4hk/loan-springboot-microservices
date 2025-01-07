package com.example.applicationserver.service.impl;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.FileStorageClient;
import com.example.applicationserver.client.JudgementClient;
import com.example.applicationserver.client.TermsClient;
import com.example.applicationserver.client.dto.*;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.controller.ApplicationController;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);
    private final ApplicationRepository applicationRepository;
    private final TermsClient termClient;
    private final AcceptTermsClient acceptTermsClient;
    private final FileStorageClient fileStorageClient;
    private final JudgementClient judgementClient;

    @Override
    public ApplicationResponseDto create(ApplicationRequestDto request) {
        logger.info("ApplicationServiceImpl - create invoked");
        logger.debug("ApplicationServiceImpl - request: {}", request);
        Application application = ApplicationMapper.mapToApplication(request);
        application.setAppliedAt(LocalDateTime.now());
        Application created = applicationRepository.save(application);
        return ApplicationMapper.mapToApplicationResponseDto(created);
    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponseDto get(Long applicationId) {
        logger.info("ApplicationServiceImpl - get invoked");
        logger.debug("ApplicationServiceImpl - applicationId: {}", applicationId);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });
        return ApplicationMapper.mapToApplicationResponseDto(application);
    }

    @Override
    public ApplicationResponseDto update(Long applicationId, ApplicationRequestDto request) {
        logger.info("ApplicationServiceImpl - update invoked");
        logger.debug("ApplicationServiceImpl - applicationId: {}", applicationId);
        logger.debug("ApplicationServiceImpl - request: {}", request);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });
        application.setFirstname(request.getFirstname());
        application.setLastname(request.getLastname());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        return ApplicationMapper.mapToApplicationResponseDto(application);
    }

    @Override
    public void delete(Long applicationId) {
        logger.info("ApplicationServiceImpl - delete invoked");
        logger.debug("ApplicationServiceImpl - applicationId: {}", applicationId);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });
        application.setIsDeleted(true);
    }

    @Override
    public void acceptTerms(Long applicationId, AcceptTermsRequestDto request) {
        logger.info("ApplicationServiceImpl - acceptTerms invoked");
        logger.debug("ApplicationServiceImpl - request: {}", request);
        get(applicationId);
        ResponseDTO<List<TermsResponseDto>> termsResponse = termClient.getAll();

        List<TermsResponseDto> terms = termsResponse.getData();

        if (terms.isEmpty()) {
            logger.error("ApplicationServiceImpl - Terms server error");
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, "Terms server error", HttpStatus.NOT_FOUND);
        }

        List<Long> requestTermsIds = request.getTermsIds();

        if (terms.size() != requestTermsIds.size()) {
            logger.error("ApplicationServiceImpl - Terms size does not match");
            throw new BaseException(ResultType.BAD_REQUEST, "Terms size does not match", HttpStatus.BAD_REQUEST);
        }

        List<Long> termsIds = terms.stream().map(TermsResponseDto::getTermsId).toList();

        if (!termsIds.containsAll(requestTermsIds)) {
            logger.error("ApplicationServiceImpl - Terms do not match");
            throw new BaseException(ResultType.BAD_REQUEST, "Terms do not match", HttpStatus.BAD_REQUEST);
        }

        AcceptTermsRequestDto.builder()
                .termsIds(requestTermsIds)
                .applicationId(applicationId)
                .build();

        acceptTermsClient.create(request);

    }

    @Override
    public void uploadFile(Long applicationId, MultipartFile file) {
        logger.info("ApplicationServiceImpl - uploadFile invoked");
        logger.debug("ApplicationServiceImpl - applicationId: {}", applicationId);
        if (!isPresentApplication(applicationId)) {
            logger.error("ApplicationServiceImpl - Application does not exist");
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
        }
        ResponseDTO<Void> fileStorageResponse = fileStorageClient.upload(applicationId, file);
    }

    @Override
    public Resource downloadFile(Long applicationId, String fileName) {
        logger.info("ApplicationServiceImpl - downloadFile invoked");
        logger.debug("ApplicationServiceImpl - fileName: {}", fileName);
        ResponseEntity<Resource> fileStorageResponse = fileStorageClient.download(applicationId, fileName);
        return fileStorageResponse.getBody();
    }

    @Override
    public List<FileResponseDto> loadAllFiles(Long applicationId) {
        logger.info("ApplicationServiceImpl - loadAllFiles invoked");
        logger.debug("ApplicationServiceImpl - applicationId: {}", applicationId);
        ResponseDTO<List<FileResponseDto>> fileStorageResponse = fileStorageClient.getFilesInfo(applicationId);
        return fileStorageResponse.getData();
    }

    @Override
    public void deleteAllFiles(Long applicationId) {
        logger.info("ApplicationServiceImpl - deleteAllFiles invoked");
        logger.debug("ApplicationServiceImpl - applicationId: {}", applicationId);
        fileStorageClient.deleteAll(applicationId);
    }

    @Override
    public void updateGrant(Long applicationId, GrantAmountDto grantAmountDto) {
        logger.info("ApplicationServiceImpl - updateGrant invoked");
        logger.debug("ApplicationServiceImpl - applicationId: {}", applicationId);
        logger.debug("ApplicationServiceImpl - grantAmountDto: {}", grantAmountDto);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });
        application.setApprovalAmount(grantAmountDto.getApprovalAmount());
    }

    @Override
    public ApplicationResponseDto contract(Long applicationId) {
        logger.info("ApplicationServiceImpl - contract invoked");
        logger.debug("ApplicationServiceImpl - applicationId: {}", applicationId);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });

        ResponseDTO<JudgementResponseDto> judgementResponse = judgementClient.getJudgmentOfApplication(applicationId);

        if (judgementResponse.getData() == null) {
            logger.error("ApplicationServiceImpl - Judgement does not exist");
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, "Judgement does not exist", HttpStatus.NOT_FOUND);
        }

        if (application.getApprovalAmount() == null || application.getApprovalAmount().compareTo(BigDecimal.ZERO) == 0) {
            logger.error("ApplicationServiceImpl - Approval amount does not exist");
            throw new BaseException(ResultType.BAD_REQUEST, "Approval amount does not exist", HttpStatus.BAD_REQUEST);
        }

        application.setContractedAt(LocalDateTime.now());
        return ApplicationMapper.mapToApplicationResponseDto(application);

    }

    private boolean isPresentApplication(Long applicationId) {
        logger.info("ApplicationServiceImpl - isPresentApplication invoked");
        logger.debug("ApplicationServiceImpl - applicationId: {}", applicationId);
        return applicationRepository.existsById(applicationId);
    }
}
