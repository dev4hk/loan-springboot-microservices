package com.example.applicationserver.service.impl;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.JudgementClient;
import com.example.applicationserver.client.TermsClient;
import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.JudgementResponseDto;
import com.example.applicationserver.client.dto.TermsResponseDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final JudgementClient judgementClient;
    private final StreamBridge streamBridge;

    @Override
    public ApplicationResponseDto create(ApplicationRequestDto request) {
        logger.info("ApplicationServiceImpl - create invoked");
        Application application = ApplicationMapper.mapToApplication(request);
        application.setAppliedAt(LocalDateTime.now());
        Application created = applicationRepository.save(application);
        sendCommunication(application);
        return ApplicationMapper.mapToApplicationResponseDto(created);
    }

    private void sendCommunication(Application application) {
        var applicationMsgDto = new ApplicationMsgDto(
                application.getApplicationId(),
                application.getFirstname(),
                application.getLastname(),
                application.getEmail(),
                application.getCellPhone()
        );
        logger.debug("Sending Communication request for the details: {}", applicationMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", applicationMsgDto);
        logger.debug("Is the Communication request successfully invoked?: {}", result);
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
        return ApplicationMapper.mapToApplicationResponseDto(application);
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
    }

    @Override
    public void acceptTerms(Long applicationId, AcceptTermsRequestDto request) {
        logger.info("ApplicationServiceImpl - acceptTerms invoked");
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
    public void updateGrant(Long applicationId, GrantAmountDto grantAmountDto) {
        logger.info("ApplicationServiceImpl - updateGrant invoked");
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

    @Override
    public boolean updateCommunicationStatus(Long applicationId) {
        logger.info("ApplicationServiceImpl - updateCommunicationStatus invoked");
        boolean isUpdated = false;
        if(applicationId != null) {
            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> {
                        logger.error("ApplicationServiceImpl - Application does not exist");
                        return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Application does not exist", HttpStatus.NOT_FOUND);
                    });
            application.setCommunicationSw(true);
            isUpdated = true;
        }
        return isUpdated;
    }

}
