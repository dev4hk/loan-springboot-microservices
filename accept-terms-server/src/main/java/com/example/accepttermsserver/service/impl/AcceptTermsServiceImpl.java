package com.example.accepttermsserver.service.impl;

import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.dto.AcceptTermsResponseDto;
import com.example.accepttermsserver.service.IAcceptTermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AcceptTermsServiceImpl implements IAcceptTermsService {
    @Override
    public AcceptTermsResponseDto create(AcceptTermsRequestDto acceptTermsRequestDto) {
        return null;
    }

    @Override
    public AcceptTermsResponseDto get(Long acceptTermsId) {
        return null;
    }

    @Override
    public AcceptTermsResponseDto update(Long acceptTermsId, AcceptTermsRequestDto acceptTermsRequestDto) {
        return null;
    }

    @Override
    public void delete(Long acceptTermsId) {

    }
}
