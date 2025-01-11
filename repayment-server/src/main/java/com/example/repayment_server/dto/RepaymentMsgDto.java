package com.example.repayment_server.dto;

import com.example.repayment_server.constants.CommunicationStatus;

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
