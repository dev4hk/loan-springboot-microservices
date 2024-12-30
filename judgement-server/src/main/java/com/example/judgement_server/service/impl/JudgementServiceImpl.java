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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class JudgementServiceImpl implements IJudgementService {

    private final JudgementRepository judgementRepository;
    private final ApplicationClient applicationClient;

    @Override
    public JudgementResponseDto create(JudgementRequestDto request) {
        Long applicationId = request.getApplicationId();
        ensureApplicationExists(applicationId);
        Judgement judgement = JudgementMapper.mapToJudgement(request);
        Judgement created = judgementRepository.save(judgement);
        return JudgementMapper.mapToJudgementResponseDto(created);
    }

    @Override
    public JudgementResponseDto get(Long judgementId) {
        Judgement judgement = judgementRepository.findById(judgementId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        return JudgementMapper.mapToJudgementResponseDto(judgement);
    }

    @Override
    public JudgementResponseDto getJudgmentOfApplication(Long applicationId) {
        ensureApplicationExists(applicationId);
        Judgement judgement = judgementRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        return JudgementMapper.mapToJudgementResponseDto(judgement);
    }

    @Override
    public JudgementResponseDto update(Long judgementId, JudgementRequestDto request) {
        ensureApplicationExists(request.getApplicationId());
        Judgement judgement = judgementRepository.findById(judgementId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        judgement.setFirstname(request.getFirstname());
        judgement.setLastname(request.getLastname());
        judgement.setApprovalAmount(request.getApprovalAmount());
        return JudgementMapper.mapToJudgementResponseDto(judgement);
    }

    @Override
    public void delete(Long judgementId) {
        Judgement judgement = judgementRepository.findById(judgementId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        judgement.setIsDeleted(true);
    }

    @Override
    public GrantAmountDto grant(Long judgementId) {
        Judgement judgement = judgementRepository.findById(judgementId).orElseThrow(() ->
                new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND)
        );

        Long applicationId = judgement.getApplicationId();
        ensureApplicationExists(applicationId);
        BigDecimal approvalAmount = judgement.getApprovalAmount();
        GrantAmountDto grantAmountDto = GrantAmountDto.builder()
                .approvalAmount(approvalAmount)
                .applicationId(applicationId)
                .build();
        applicationClient.updateGrant(applicationId, grantAmountDto);
        return grantAmountDto;
    }

    private void ensureApplicationExists(Long applicationId) {
        ResponseDTO<ApplicationResponseDto> response = applicationClient.get(applicationId);
        if (response == null || response.getData() == null) {
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}


