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

    public static TermsResponseDto mapToTermsResponseDto(Terms created) {
        return TermsResponseDto.builder()
                .termsId(created.getTermsId())
                .name(created.getName())
                .termsDetailUrl(created.getTermsDetailUrl())
                .build();
    }

}
