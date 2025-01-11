package com.example.applicationserver.dto;

import com.example.applicationserver.constants.CommunicationStatus;

public record ApplicationMsgDto(
        Long applicationId,
        String firstname,
        String lastname,
        String email,
        String cellPhone,
        CommunicationStatus communicationStatus
) {
}

