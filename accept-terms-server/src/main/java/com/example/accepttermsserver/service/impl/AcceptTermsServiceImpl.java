package com.example.accepttermsserver.service.impl;

import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.dto.AcceptTermsResponseDto;
import com.example.accepttermsserver.service.IAcceptTermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AcceptTermsServiceImpl implements IAcceptTermsService {
    @Override
    public AcceptTermsResponseDto create(AcceptTermsRequestDto acceptTermsRequestDto) {
        return null;
    }

}
