package com.example.applicationserver.mapper;

import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.entity.Application;

public class ApplicationMapper {

    public static Application mapToApplication(ApplicationRequestDto applicationRequestDto) {
        return Application.builder()
                .firstname(applicationRequestDto.getFirstname())
                .lastname(applicationRequestDto.getLastname())
                .cellPhone(applicationRequestDto.getCellPhone())
                .email(applicationRequestDto.getEmail())
                .hopeAmount(applicationRequestDto.getHopeAmount())
                .build();
    }

    public static ApplicationResponseDto mapToApplicationResponseDto(Application application) {
        return ApplicationResponseDto.builder()
                .applicationId(application.getApplicationId())
                .firstname(application.getFirstname())
                .lastname(application.getLastname())
                .cellPhone(application.getCellPhone())
                .email(application.getEmail())
                .hopeAmount(application.getHopeAmount())
                .appliedAt(application.getAppliedAt())
                .contractedAt(application.getContractedAt())
                .createdAt(application.getCreatedAt())
                .createdBy(application.getCreatedBy())
                .updatedAt(application.getUpdatedAt())
                .updatedBy(application.getUpdatedBy())
                .build();
    }
}
