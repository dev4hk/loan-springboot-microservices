package com.example.accepttermsserver.mapper;

import com.example.accepttermsserver.dto.AcceptTermsResponseDto;
import com.example.accepttermsserver.entity.AcceptTerms;

public class AcceptTermsMapper {

    public static AcceptTerms mapToEntity(Long applicationId, Long termsId) {
        return AcceptTerms.builder()
                .termsId(termsId)
                .applicationId(applicationId)
                .build();
    }

    public static AcceptTermsResponseDto mapToResponseDto(AcceptTerms acceptTerms) {
        return AcceptTermsResponseDto.builder()
                .acceptTermsId(acceptTerms.getAcceptTermsId())
                .applicationId(acceptTerms.getApplicationId())
                .termsId(acceptTerms.getTermsId())
                .createdAt(acceptTerms.getCreatedAt())
                .createdBy(acceptTerms.getCreatedBy())
                .updatedAt(acceptTerms.getUpdatedAt())
                .updatedBy(acceptTerms.getUpdatedBy())
                .build();
    }

}
