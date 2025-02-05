package com.example.applicationserver.service.impl;

import com.example.applicationserver.client.*;
import com.example.applicationserver.client.dto.*;
import com.example.applicationserver.constants.CommunicationStatus;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.*;
import com.example.applicationserver.entity.Application;
import com.example.applicationserver.exception.BaseException;
import com.example.applicationserver.mapper.ApplicationMapper;
import com.example.applicationserver.repository.ApplicationRepository;
import com.example.applicationserver.service.IApplicationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements IApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);
    private final ApplicationRepository applicationRepository;
    private final TermsClient termClient;
    private final AcceptTermsClient acceptTermsClient;
    private final CounselClient counselClient;
    private final JudgementClient judgementClient;
    private final FileStorageClient fileStorageClient;
    private final StreamBridge streamBridge;

    @Override
    public ApplicationResponseDto create(ApplicationRequestDto applicationRequestDto, AcceptTermsRequestDto acceptTermsRequestDto) {
        logger.info("ApplicationServiceImpl - create invoked");
        applicationRepository.findByEmail(applicationRequestDto.getEmail())
                .ifPresent(application -> {
                    throw new BaseException(ResultType.CONFLICT, HttpStatus.CONFLICT);
                });
        Application application = ApplicationMapper.mapToApplication(applicationRequestDto);
        application.setAppliedAt(LocalDateTime.now());
        Application created = applicationRepository.save(application);
        acceptTerms(created.getApplicationId(), acceptTermsRequestDto);

        ApplicationResponseDto responseDto = ApplicationMapper.mapToApplicationResponseDto(created);
        ResponseDTO<CounselResponseDto> counselResponse = counselClient.getByEmail(application.getEmail());
        if (counselResponse != null) {
            responseDto.setCounselInfo(counselResponse.getData());
        }
        sendCommunication(created, CommunicationStatus.APPLICATION_RECEIVED);
        return responseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponseDto get(Long applicationId) {
        logger.info("ApplicationServiceImpl - get invoked");
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });

        ResponseDTO<CounselResponseDto> counselResponse = counselClient.getByEmail(application.getEmail());
        ResponseDTO<List<FileResponseDto>> fileStorageResponse = fileStorageClient.getFilesInfo(applicationId);

        ApplicationResponseDto responseDto = ApplicationMapper.mapToApplicationResponseDto(application);
        if (counselResponse != null) {
            responseDto.setCounselInfo(counselResponse.getData());
        }
        if (fileStorageResponse != null) {
            responseDto.setFileInfo(fileStorageResponse.getData());
        }
        return responseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponseDto getByEmail(String email) {
        logger.info("ApplicationServiceImpl - getByEmail invoked");
        Application application = applicationRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });

        ResponseDTO<CounselResponseDto> counselResponse = counselClient.getByEmail(application.getEmail());
        ResponseDTO<List<FileResponseDto>> fileStorageResponse = fileStorageClient.getFilesInfo(application.getApplicationId());

        ApplicationResponseDto responseDto = ApplicationMapper.mapToApplicationResponseDto(application);
        if (counselResponse != null) {
            responseDto.setCounselInfo(counselResponse.getData());
        }
        if (fileStorageResponse != null) {
            responseDto.setFileInfo(fileStorageResponse.getData());
        }
        return responseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ApplicationResponseDto> getAll(Pageable pageable) {
        logger.info("ApplicationServiceImpl - getAll invoked");
        return applicationRepository.findAll(pageable)
                .map(ApplicationMapper::mapToApplicationResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<CommunicationStatus, Long> getApplicationStatistics() {
        logger.info("ApplicationServiceImpl - getApplicationStatistics invoked");
        return applicationRepository.getCommunicationStatusStats()
                .stream().collect(Collectors.toMap(CommunicationStatusStats::getCommunicationStatus, CommunicationStatusStats::getCount));
    }

    @Override
    public void complete(Long applicationId) {
        logger.info("ApplicationServiceImpl - complete invoked");
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });
        sendCommunication(application, CommunicationStatus.APPLICATION_REPAYMENT_COMPLETE);
    }

    @Override
    public ApplicationResponseDto update(Long applicationId, ApplicationRequestDto request) {
        logger.info("ApplicationServiceImpl - update invoked");
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

        sendCommunication(application, CommunicationStatus.APPLICATION_UPDATED);

        return ApplicationMapper.mapToApplicationResponseDto(application);
    }

    @Override
    public void delete(Long applicationId) {
        logger.info("ApplicationServiceImpl - delete invoked");
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });
        application.setIsDeleted(true);
        sendCommunication(application, CommunicationStatus.APPLICATION_REMOVED);
    }

    @Override
    public void acceptTerms(Long applicationId, AcceptTermsRequestDto request) {
        logger.info("ApplicationServiceImpl - acceptTerms invoked");

        ResponseDTO<List<TermsResponseDto>> termsResponse = termClient.getAll();

        List<TermsResponseDto> terms = termsResponse.getData();
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

        request.setApplicationId(applicationId);

        acceptTermsClient.create(request);

    }

    @Override
    public void updateGrant(Long applicationId, GrantAmountDto grantAmountDto) {
        logger.info("ApplicationServiceImpl - updateGrant invoked");
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });
        application.setApprovalAmount(grantAmountDto.getApprovalAmount());
        sendCommunication(application, CommunicationStatus.APPLICATION_GRANT_UPDATED);
    }

    @Override
    public ApplicationResponseDto contract(Long applicationId) {
        logger.info("ApplicationServiceImpl - contract invoked");
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });

        ResponseDTO<JudgementResponseDto> judgementResponse = judgementClient.getJudgmentOfApplication(applicationId);

        if (application.getApprovalAmount() == null || application.getApprovalAmount().compareTo(BigDecimal.ZERO) == 0) {
            logger.error("ApplicationServiceImpl - Approval amount does not exist");
            throw new BaseException(ResultType.BAD_REQUEST, "Approval amount does not exist", HttpStatus.BAD_REQUEST);
        }

        application.setContractedAt(LocalDateTime.now());
        sendCommunication(application, CommunicationStatus.APPLICATION_CONTRACTED);
        return ApplicationMapper.mapToApplicationResponseDto(application);

    }

    private void sendCommunication(Application application, CommunicationStatus communicationStatus) {
        var applicationMsgDto = new ApplicationMsgDto(
                application.getApplicationId(),
                application.getFirstname(),
                application.getLastname(),
                application.getEmail(),
                application.getCellPhone(),
                communicationStatus
        );
        logger.debug("Sending Communication request for the details: {}", applicationMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", applicationMsgDto);
        logger.debug("Is the Communication request successfully invoked?: {}", result);
    }

    @Override
    public void updateCommunicationStatus(Long applicationId, CommunicationStatus communicationStatus) {
        logger.info("ApplicationServiceImpl - updateCommunicationStatus invoked");

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("ApplicationServiceImpl - Application does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                });
        application.setCommunicationStatus(communicationStatus);
    }



}
