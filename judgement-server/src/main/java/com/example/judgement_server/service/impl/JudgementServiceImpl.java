package com.example.judgement_server.service.impl;

import com.example.judgement_server.client.ApplicationClient;
import com.example.judgement_server.client.dto.ApplicationResponseDto;
import com.example.judgement_server.constants.ResultType;
import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;
import com.example.judgement_server.dto.ResponseDTO;
import com.example.judgement_server.entity.Judgement;
import com.example.judgement_server.exception.BaseException;
import com.example.judgement_server.mapper.JudgementMapper;
import com.example.judgement_server.repository.JudgementRepository;
import com.example.judgement_server.service.IJudgementService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public JudgementResponseDto create(JudgementRequestDto request) {
        logger.info("JudgementServiceImpl - create invoked");
        Long applicationId = request.getApplicationId();
        ensureApplicationExists(applicationId);
        Judgement judgement = JudgementMapper.mapToJudgement(request);

        updatePayment(request, judgement);

        Judgement created = judgementRepository.save(judgement);
        return JudgementMapper.mapToJudgementResponseDto(created);
    }

    private void updatePayment(JudgementRequestDto request, Judgement judgement) {
        int numberOfPayments = getNumberOfPayments(request.getStartDate(), request.getEndDate(), request.getPayDay());
        BigDecimal total = request.getApprovalAmount().multiply(request.getInterest());
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
        ensureApplicationExists(applicationId);
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
        ensureApplicationExists(request.getApplicationId());
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
        updatePayment(request, judgement);
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
        judgement.setIsDeleted(true);
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
        ensureApplicationExists(applicationId);
        BigDecimal approvalAmount = judgement.getApprovalAmount();
        GrantAmountDto grantAmountDto = GrantAmountDto.builder()
                .approvalAmount(approvalAmount)
                .applicationId(applicationId)
                .build();
        ResponseDTO<Void> applicationResponseDto = applicationClient.updateGrant(applicationId, grantAmountDto);
        return grantAmountDto;
    }

    private void ensureApplicationExists(Long applicationId) {
        logger.info("JudgementServiceImpl - ensureApplicationExists invoked");
        ResponseDTO<ApplicationResponseDto> response = applicationClient.get(applicationId);
    }
}


