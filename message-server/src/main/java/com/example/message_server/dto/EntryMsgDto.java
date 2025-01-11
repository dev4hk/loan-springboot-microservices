package com.example.message_server.dto;

import com.example.message_server.constant.CommunicationStatus;

import java.math.BigDecimal;

public record EntryMsgDto(
        Long entryId,
        Long applicationId,
        BigDecimal entryAmount,
        String firstname,
        String lastname,
        String cellPhone,
        String email,
        CommunicationStatus communicationStatus
) {
}
