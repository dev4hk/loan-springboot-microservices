package com.example.message_server.dto;

public record ApplicationMsgDto(
        Long applicationId,
        String firstname,
        String lastname,
        String email,
        String cellPhone
) {
}
