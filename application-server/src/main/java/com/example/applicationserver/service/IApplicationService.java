package com.example.applicationserver.service;

import com.example.applicationserver.cllient.dto.AcceptTermsRequestDto;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;

public interface IApplicationService {
    ApplicationResponseDto create(ApplicationRequestDto request);

    ApplicationResponseDto get(Long findId);

    ApplicationResponseDto update(Long applicationId, ApplicationRequestDto request);

    void delete(Long applicationId);

    void acceptTerms(Long applicationId, AcceptTermsRequestDto request);
}
