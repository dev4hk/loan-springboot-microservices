package com.example.counselserver.dto;

import com.example.counselserver.constants.CommunicationStatus;

public record CounselMsgDto(
        Long counselId,
        String firstname,
        String lastname,
        String cellPhone,
        String email,
        CommunicationStatus communicationStatus
) {
}
