package com.example.judgement_server.dto;

import com.example.judgement_server.constants.CommunicationStatus;

import java.math.BigDecimal;

public record JudgementMsgDto (
        Long JudgementId,
        Long applicationId,
        BigDecimal approvalAmount,
        String firstname,
        String lastname,
        String email,
        String cellPhone,
        CommunicationStatus communicationStatus
) {
}
