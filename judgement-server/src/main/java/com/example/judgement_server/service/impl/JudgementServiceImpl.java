package com.example.judgement_server.service.impl;

import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;
import com.example.judgement_server.service.IJudgementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JudgementServiceImpl implements IJudgementService {

    @Override
    public JudgementResponseDto create(JudgementRequestDto request) {
        return null;
    }

    @Override
    public JudgementResponseDto get(Long judgmentId) {
        return null;
    }

    @Override
    public JudgementResponseDto getJudgmentOfApplication(Long applicationId) {
        return null;
    }

    @Override
    public JudgementResponseDto update(Long judgmentId, JudgementRequestDto request) {
        return null;
    }

    @Override
    public void delete(Long judgmentId) {

    }
}
