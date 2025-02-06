package com.example.message_server.dto;


import com.example.message_server.constant.CommunicationStatus;

import java.math.BigDecimal;

public record JudgementMsgDto(
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
