package com.example.termsserver.service;

import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;

import java.util.List;

public interface ITermsService {
    TermsResponseDto create(TermsRequestDto request);

    List<TermsResponseDto> getAll();

    TermsResponseDto get(Long termsId);

    TermsResponseDto update(Long termsId, TermsRequestDto request);

    void delete(Long termsId);
}
