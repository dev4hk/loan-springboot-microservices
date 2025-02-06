package com.example.judgement_server.service.impl;

import com.example.judgement_server.client.ApplicationClient;
import com.example.judgement_server.client.dto.ApplicationResponseDto;
import com.example.judgement_server.constants.CommunicationStatus;
import com.example.judgement_server.constants.ResultType;
import com.example.judgement_server.dto.*;
import com.example.judgement_server.entity.Judgement;
import com.example.judgement_server.exception.BaseException;
import com.example.judgement_server.mapper.JudgementMapper;
import com.example.judgement_server.repository.JudgementRepository;
import com.example.judgement_server.service.IJudgementService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Transactional
@RequiredArgsConstructor
@Service
public class JudgementServiceImpl implements IJudgementService {

    private static final Logger logger = LoggerFactory.getLogger(JudgementServiceImpl.class);
    private final JudgementRepository judgementRepository;
    private final ApplicationClient applicationClient;
    private final StreamBridge streamBridge;

    @Override
    public JudgementResponseDto create(JudgementRequestDto request) {
        logger.info("JudgementServiceImpl - create invoked");
        Long applicationId = request.getApplicationId();
        ApplicationResponseDto applicationResponseDto = getApplication(applicationId);
        Judgement judgement = JudgementMapper.mapToJudgement(request);

        updatePayment(request, judgement);

        Judgement created = judgementRepository.save(judgement);
        sendCommunication(created, applicationResponseDto, CommunicationStatus.JUDGEMENT_CREATED);
        return JudgementMapper.mapToJudgementResponseDto(created);
    }

    private void updatePayment(JudgementRequestDto request, Judgement judgement) {
        int numberOfPayments = getNumberOfPayments(request.getStartDate(), request.getEndDate(), request.getPayDay());
        BigDecimal total = request.getApprovalAmount().multiply(request.getInterest().add(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = total.divide(
                BigDecimal.valueOf(numberOfPayments),
                2,
                RoundingMode.HALF_UP
        );
        judgement.setNumberOfPayments(numberOfPayments);
        judgement.setTotal(total);
        judgement.setMonthlyPayment(monthlyPayment);
    }

    int getNumberOfPayments(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer payDay) {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();

        YearMonth ym = YearMonth.from(startDate);
        LocalDate curr = ym.atDay(payDay);

        if (curr.isBefore(startDate)) {
            ym = ym.plusMonths(1);
            curr = ym.atDay(payDay);
        }

        int count = 0;

        while (!curr.isAfter(endDate)) {
            count++;
            ym = ym.plusMonths(1);
            curr = ym.atDay(payDay);
        }

        return count;
    }

    @Transactional(readOnly = true)
    @Override
    public JudgementResponseDto get(Long judgementId) {
        logger.info("JudgementServiceImpl - get invoked");
        Judgement judgement = judgementRepository.findById(judgementId)
                .orElseThrow(() ->
                        {
                            logger.error("JudgementServiceImpl - Judgement does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Judgement does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        return JudgementMapper.mapToJudgementResponseDto(judgement);
    }

    @Transactional(readOnly = true)
    @Override
    public JudgementResponseDto getJudgementOfApplication(Long applicationId) {
        logger.info("JudgementServiceImpl - getJudgmentOfApplication invoked");
        getApplication(applicationId);
        Judgement judgement = judgementRepository.findByApplicationId(applicationId)
                .orElseThrow(() ->
                        {
                            logger.error("JudgementServiceImpl - Judgement does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Judgement does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        return JudgementMapper.mapToJudgementResponseDto(judgement);
    }

    @Override
    public JudgementResponseDto update(Long judgementId, JudgementRequestDto request) {
        logger.info("JudgementServiceImpl - update invoked");
        ApplicationResponseDto applicationResponseDto = getApplication(request.getApplicationId());
        Judgement judgement = judgementRepository.findById(judgementId)
                .orElseThrow(() ->
                        {
                            logger.error("JudgementServiceImpl - Judgement does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Judgement does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        judgement.setFirstname(request.getFirstname());
        judgement.setLastname(request.getLastname());
        judgement.setApprovalAmount(request.getApprovalAmount());
        judgement.setStartDate(request.getStartDate());
        judgement.setEndDate(request.getEndDate());
        judgement.setPayDay(request.getPayDay());
        judgement.setInterest(request.getInterest());
        updatePayment(request, judgement);
        sendCommunication(judgement, applicationResponseDto, CommunicationStatus.JUDGEMENT_UPDATED);
        return JudgementMapper.mapToJudgementResponseDto(judgement);
    }

    @Override
    public void delete(Long judgementId) {
        logger.info("JudgementServiceImpl - delete invoked");
        Judgement judgement = judgementRepository.findById(judgementId)
                .orElseThrow(() ->
                        {
                            logger.error("JudgementServiceImpl - Judgement does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Judgement does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        ApplicationResponseDto applicationResponseDto = getApplication(judgement.getApplicationId());
        judgement.setIsDeleted(true);
        sendCommunication(judgement, applicationResponseDto, CommunicationStatus.JUDGEMENT_REMOVED);
    }

    @Override
    public GrantAmountDto grant(Long judgementId) {
        logger.info("JudgementServiceImpl - grant invoked");
        Judgement judgement = judgementRepository.findById(judgementId).orElseThrow(() ->
                {
                    logger.error("JudgementServiceImpl - Judgement does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Judgement does not exist", HttpStatus.NOT_FOUND);
                }
        );

        Long applicationId = judgement.getApplicationId();
        ApplicationResponseDto applicationResponseDto = getApplication(applicationId);
        BigDecimal approvalAmount = judgement.getApprovalAmount();
        GrantAmountDto grantAmountDto = GrantAmountDto.builder()
                .approvalAmount(approvalAmount)
                .applicationId(applicationId)
                .build();
        applicationClient.updateGrant(applicationId, grantAmountDto);
        sendCommunication(judgement, applicationResponseDto, CommunicationStatus.JUDGEMENT_COMPLETE);
        return grantAmountDto;
    }

    @Override
    public void updateCommunicationStatus(Long judgementId, CommunicationStatus communicationStatus) {
        logger.info("JudgementServiceImpl - updateCommunicationStatus invoked");

        Judgement judgement = judgementRepository.findById(judgementId).orElseThrow(() ->
                {
                    logger.error("JudgementServiceImpl - Judgement does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Judgement does not exist", HttpStatus.NOT_FOUND);
                }
        );
        judgement.setCommunicationStatus(communicationStatus);

    }

    private void sendCommunication(Judgement judgement, ApplicationResponseDto applicationResponseDto, CommunicationStatus communicationStatus) {
        var judgementMsgDto = new JudgementMsgDto(
                judgement.getJudgementId(),
                judgement.getApplicationId(),
                judgement.getApprovalAmount(),
                applicationResponseDto.getFirstname(),
                applicationResponseDto.getLastname(),
                applicationResponseDto.getCellPhone(),
                applicationResponseDto.getEmail(),
                communicationStatus
        );

        logger.debug("Sending Communication request for the details: {}", judgementMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", judgementMsgDto);
        logger.debug("Is the Communication request successfully invoked?: {}", result);
    }

    private ApplicationResponseDto getApplication(Long applicationId) {
        logger.info("JudgementServiceImpl - getApplication invoked");
        ResponseDTO<ApplicationResponseDto> response = applicationClient.get(applicationId);
        return response.getData();

    }
}


