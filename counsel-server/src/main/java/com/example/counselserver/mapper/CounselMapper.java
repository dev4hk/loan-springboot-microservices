package com.example.counselserver.mapper;

import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;
import com.example.counselserver.entity.Counsel;

public class CounselMapper {

    public static Counsel mapToCounsel(CounselRequestDto request) {
        return Counsel.builder()
                .name(request.getName())
                .cellPhone(request.getCellPhone())
                .email(request.getEmail())
                .memo(request.getMemo())
                .zipCode(request.getZipCode())
                .address(request.getAddress())
                .addressDetail(request.getAddressDetail())
                .build();
    }

    public static CounselResponseDto mapToCounselResponseDto(Counsel counsel) {
        return CounselResponseDto.builder()
                .counselId(counsel.getCounselId())
                .name(counsel.getName())
                .cellPhone(counsel.getCellPhone())
                .email(counsel.getEmail())
                .memo(counsel.getMemo())
                .zipCode(counsel.getZipCode())
                .address(counsel.getAddress())
                .addressDetail(counsel.getAddressDetail())
                .appliedAt(counsel.getAppliedAt())
                .createdAt(counsel.getCreatedAt())
                .createdBy(counsel.getCreatedBy())
                .updatedAt(counsel.getUpdatedAt())
                .updatedBy(counsel.getUpdatedBy())
                .build();
    }
}
