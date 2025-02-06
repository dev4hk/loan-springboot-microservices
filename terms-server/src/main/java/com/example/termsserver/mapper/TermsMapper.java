package com.example.termsserver.mapper;

import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.entity.Terms;

public class TermsMapper {

    public static Terms mapToEntity(TermsRequestDto request) {
        return Terms.builder()
                .name(request.getName())
                .termsDetail(request.getTermsDetail())
                .build();
    }

    public static TermsResponseDto mapToTermsResponseDto(Terms terms) {
        return TermsResponseDto.builder()
                .termsId(terms.getTermsId())
                .name(terms.getName())
                .termsDetail(terms.getTermsDetail())
                .createdAt(terms.getCreatedAt())
                .createdBy(terms.getCreatedBy())
                .updatedAt(terms.getUpdatedAt())
                .updatedBy(terms.getUpdatedBy())
                .build();
    }

}
