package com.example.counselserver.mapper;

import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;
import com.example.counselserver.entity.Counsel;

public class CounselMapper {

    public static Counsel mapToCounsel(CounselRequestDto request) {
        return Counsel.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .cellPhone(request.getCellPhone())
                .email(request.getEmail())
                .memo(request.getMemo())
                .zipCode(request.getZipCode())
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .city(request.getCity())
                .state(request.getState())
                .build();
    }

    public static CounselResponseDto mapToCounselResponseDto(Counsel counsel) {
        return CounselResponseDto.builder()
                .counselId(counsel.getCounselId())
                .firstname(counsel.getFirstname())
                .lastname(counsel.getLastname())
                .cellPhone(counsel.getCellPhone())
                .email(counsel.getEmail())
                .memo(counsel.getMemo())
                .zipCode(counsel.getZipCode())
                .address1(counsel.getAddress1())
                .address2(counsel.getAddress2())
                .city(counsel.getCity())
                .state(counsel.getState())
                .appliedAt(counsel.getAppliedAt())
                .communicationStatus(counsel.getCommunicationStatus())
                .createdAt(counsel.getCreatedAt())
                .createdBy(counsel.getCreatedBy())
                .updatedAt(counsel.getUpdatedAt())
                .updatedBy(counsel.getUpdatedBy())
                .build();
    }
}
