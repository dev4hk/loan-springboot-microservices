package com.example.termsserver.service.impl;

import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.service.ITermsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TermsServiceImpl implements ITermsService {

    @Override
    public TermsResponseDto create(TermsRequestDto request) {
        return null;
    }

    @Override
    public List<TermsResponseDto> getAll() {
        return List.of();
    }
}
