package com.example.judgement_server.mapper;

import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;
import com.example.judgement_server.entity.Judgement;

public class JudgementMapper {

    public static JudgementResponseDto mapToJudgementResponseDto(Judgement judgement) {
        return JudgementResponseDto.builder()
                .applicationId(judgement.getApplicationId())
                .firstname(judgement.getFirstname())
                .lastname(judgement.getLastname())
                .approvalAmount(judgement.getApprovalAmount())
                .createdAt(judgement.getCreatedAt())
                .createdBy(judgement.getCreatedBy())
                .updatedAt(judgement.getUpdatedAt())
                .updatedBy(judgement.getUpdatedBy())
                .build();
    }

    public static Judgement mapToJudgement(JudgementRequestDto judgementRequestDto) {
        return Judgement.builder()
                .applicationId(judgementRequestDto.getApplicationId())
                .firstname(judgementRequestDto.getFirstname())
                .lastname(judgementRequestDto.getLastname())
                .approvalAmount(judgementRequestDto.getApprovalAmount())
                .build();
    }
}
