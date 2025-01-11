package com.example.message_server.dto;


import com.example.message_server.constant.CommunicationStatus;

public record CounselMsgDto(
        Long counselId,
        String firstname,
        String lastname,
        String cellPhone,
        String email,
        CommunicationStatus communicationStatus
) {
}
