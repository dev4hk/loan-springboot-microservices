package com.example.applicationserver.service;

import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.GrantAmountDto;

public interface IApplicationService {
    ApplicationResponseDto create(ApplicationRequestDto request);

    ApplicationResponseDto get(Long findId);

    ApplicationResponseDto update(Long applicationId, ApplicationRequestDto request);

    void delete(Long applicationId);

    void acceptTerms(Long applicationId, AcceptTermsRequestDto request);

    void updateGrant(Long applicationId, GrantAmountDto grantAmountDto);

    ApplicationResponseDto contract(Long applicationId);
}
