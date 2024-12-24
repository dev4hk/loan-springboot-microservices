package com.example.counselserver.service;

import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;

public interface ICounselService {
    CounselResponseDto create(CounselRequestDto request);

    CounselResponseDto get(Long counselId);

    CounselResponseDto update(Long counselId, CounselRequestDto request);

    void delete(Long counselId);
}
