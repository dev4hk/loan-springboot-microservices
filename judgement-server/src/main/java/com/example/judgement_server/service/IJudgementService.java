package com.example.judgement_server.service;

import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;

public interface IJudgementService {
    JudgementResponseDto create(JudgementRequestDto request);
    JudgementResponseDto get(Long judgmentId);
    JudgementResponseDto getJudgmentOfApplication(Long applicationId);
    JudgementResponseDto update(Long judgmentId, JudgementRequestDto request);
    void delete(Long judgmentId);
}
