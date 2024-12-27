package com.example.termsserver.service.impl;

import com.example.termsserver.constants.ResultType;
import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.entity.Terms;
import com.example.termsserver.exception.BaseException;
import com.example.termsserver.mapper.TermsMapper;
import com.example.termsserver.repository.TermsRepository;
import com.example.termsserver.service.ITermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class TermsServiceImpl implements ITermsService {

    private final TermsRepository termsRepository;

    @Override
    public TermsResponseDto create(TermsRequestDto request) {
        Terms terms = TermsMapper.mapToEntity(request);
        Terms created = termsRepository.save(terms);
        return TermsMapper.mapToTermsResponseDto(created);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TermsResponseDto> getAll() {
        List<Terms> termsList = termsRepository.findAll();
        return termsList.stream().map(TermsMapper::mapToTermsResponseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TermsResponseDto get(Long termsId) {
        Terms terms = termsRepository.findById(termsId).orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        return TermsMapper.mapToTermsResponseDto(terms);
    }

    @Override
    public TermsResponseDto update(Long termsId, TermsRequestDto request) {
        Terms terms = termsRepository.findById(termsId).orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        terms.setName(request.getName());
        terms.setTermsDetailUrl(request.getTermsDetailUrl());
        return TermsMapper.mapToTermsResponseDto(terms);
    }

    @Override
    public void delete(Long termsId) {
        Terms terms = termsRepository.findById(termsId).orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        terms.setIsDeleted(true);
    }

}
