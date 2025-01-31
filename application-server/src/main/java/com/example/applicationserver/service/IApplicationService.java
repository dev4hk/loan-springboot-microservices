package com.example.applicationserver.service;

import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.constants.CommunicationStatus;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.GrantAmountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IApplicationService {
    ApplicationResponseDto create(ApplicationRequestDto request);

    ApplicationResponseDto get(Long findId);

    ApplicationResponseDto update(Long applicationId, ApplicationRequestDto request);

    void delete(Long applicationId);

    void acceptTerms(Long applicationId, AcceptTermsRequestDto request);

    void updateGrant(Long applicationId, GrantAmountDto grantAmountDto);

    ApplicationResponseDto contract(Long applicationId);

    void updateCommunicationStatus(Long applicationId, CommunicationStatus communicationStatus);

    ApplicationResponseDto getByEmail(String email);

    Page<ApplicationResponseDto> getAll(Pageable pageable);
}
