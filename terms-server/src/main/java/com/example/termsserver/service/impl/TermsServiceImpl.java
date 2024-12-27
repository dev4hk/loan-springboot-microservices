package com.example.termsserver.service.impl;

import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.entity.Terms;
import com.example.termsserver.mapper.TermsMapper;
import com.example.termsserver.repository.TermsRepository;
import com.example.termsserver.service.ITermsService;
import lombok.RequiredArgsConstructor;
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

}
