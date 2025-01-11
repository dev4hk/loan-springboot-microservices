package com.example.message_server.dto;

import com.example.message_server.constant.CommunicationStatus;

import java.math.BigDecimal;

public record RepaymentMsgDto(
        Long repaymentId,
        Long applicationId,
        BigDecimal repaymentAmount,
        String firstname,
        String lastname,
        String cellPhone,
        String email,
        CommunicationStatus communicationStatus
) {
}
