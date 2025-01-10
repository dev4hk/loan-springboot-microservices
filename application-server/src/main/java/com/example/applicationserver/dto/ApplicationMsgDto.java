package com.example.applicationserver.dto;

public record ApplicationMsgDto(
        Long applicationId,
        String firstname,
        String lastname,
        String email,
        String cellPhone
) {
}

