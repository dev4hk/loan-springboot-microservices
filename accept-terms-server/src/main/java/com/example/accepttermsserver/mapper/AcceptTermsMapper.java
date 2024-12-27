package com.example.accepttermsserver.mapper;

import com.example.accepttermsserver.entity.AcceptTerms;

public class AcceptTermsMapper {

    public static AcceptTerms mapToEntity(Long applicationId, Long termsId) {
        return AcceptTerms.builder()
                .termsId(termsId)
                .applicationId(applicationId)
                .build();
    }

}
