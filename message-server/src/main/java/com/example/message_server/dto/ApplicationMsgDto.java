package com.example.message_server.dto;

import com.example.message_server.constant.CommunicationStatus;

public record ApplicationMsgDto(
        Long applicationId,
        String firstname,
        String lastname,
        String email,
        String cellPhone,
        CommunicationStatus communicationStatus
) {
}
