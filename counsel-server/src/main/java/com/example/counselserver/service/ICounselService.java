package com.example.counselserver.service;

import com.example.counselserver.constants.CommunicationStatus;
import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ICounselService {
    CounselResponseDto create(CounselRequestDto request);

    CounselResponseDto get(Long counselId);

    CounselResponseDto update(Long counselId, CounselRequestDto request);

    void delete(Long counselId);

    void updateCommunicationStatus(Long counselId, CommunicationStatus communicationStatus);

    CounselResponseDto getByEmail(String email);

    Page<CounselResponseDto> getAll(Pageable pageable);

    void complete(Long counselId);

    Map<CommunicationStatus, Long> getCounselStatistics();

    List<CounselResponseDto> getNewCounsels();
}
