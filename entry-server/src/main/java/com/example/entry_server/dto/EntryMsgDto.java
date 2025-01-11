package com.example.entry_server.dto;

import com.example.entry_server.constants.CommunicationStatus;

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