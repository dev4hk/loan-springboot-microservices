package com.example.termsserver.mapper;

import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.entity.Terms;

public class TermsMapper {

    public static Terms mapToEntity(TermsRequestDto request) {
        return Terms.builder()
                .name(request.getName())
                .termsDetailUrl(request.getTermsDetailUrl())
                .build();
    }

    public static TermsResponseDto mapToTermsResponseDto(Terms terms) {
        return TermsResponseDto.builder()
                .termsId(terms.getTermsId())
                .name(terms.getName())
                .termsDetailUrl(terms.getTermsDetailUrl())
                .createdAt(terms.getCreatedAt())
                .createdBy(terms.getCreatedBy())
                .updatedAt(terms.getUpdatedAt())
                .updatedBy(terms.getUpdatedBy())
                .build();
    }

}
