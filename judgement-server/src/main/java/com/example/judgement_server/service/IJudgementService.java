package com.example.judgement_server.service;

import com.example.judgement_server.constants.CommunicationStatus;
import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;

import java.util.Map;

public interface IJudgementService {
    JudgementResponseDto create(JudgementRequestDto request);
    JudgementResponseDto get(Long judgementId);
    JudgementResponseDto getJudgementOfApplication(Long applicationId);
    JudgementResponseDto update(Long judgementId, JudgementRequestDto request);
    void delete(Long judgementId);
    GrantAmountDto grant(Long judgementId);
    void updateCommunicationStatus(Long judgementId, CommunicationStatus communicationStatus);
    Map<CommunicationStatus, Long> getJudgementStatistics();
}
