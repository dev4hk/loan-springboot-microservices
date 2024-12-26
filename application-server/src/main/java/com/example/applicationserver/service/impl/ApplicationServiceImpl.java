package com.example.applicationserver.service.impl;

import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.service.IApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements IApplicationService {

    @Override
    public ApplicationResponseDto create(ApplicationRequestDto request) {
        return null;
    }

    @Override
    public ApplicationResponseDto get(Long findId) {
        return null;
    }

    @Override
    public ApplicationResponseDto update(Long applicationId, ApplicationRequestDto request) {
        return null;
    }

    @Override
    public void delete(Long applicationId) {

    }
}
