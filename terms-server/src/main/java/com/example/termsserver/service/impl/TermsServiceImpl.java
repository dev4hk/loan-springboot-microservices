package com.example.termsserver.service.impl;

import com.example.termsserver.constants.ResultType;
import com.example.termsserver.controller.TermsController;
import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.entity.Terms;
import com.example.termsserver.exception.BaseException;
import com.example.termsserver.mapper.TermsMapper;
import com.example.termsserver.repository.TermsRepository;
import com.example.termsserver.service.ITermsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class TermsServiceImpl implements ITermsService {

    private static final Logger logger = LoggerFactory.getLogger(TermsServiceImpl.class);
    private final TermsRepository termsRepository;

    @Override
    public TermsResponseDto create(TermsRequestDto request) {
        logger.info("TermsServiceImpl - create invoked");
        logger.debug("TermsServiceImpl - request: {}", request);
        Terms terms = TermsMapper.mapToEntity(request);
        Terms created = termsRepository.save(terms);
        return TermsMapper.mapToTermsResponseDto(created);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TermsResponseDto> getAll() {
        logger.info("TermsServiceImpl - getAll invoked");
        List<Terms> termsList = termsRepository.findAll();
        return termsList.stream().map(TermsMapper::mapToTermsResponseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TermsResponseDto get(Long termsId) {
        logger.info("TermsServiceImpl - get invoked");
        logger.debug("TermsServiceImpl - termsId: {}", termsId);
        Terms terms = termsRepository.findById(termsId)
                .orElseThrow(() ->
                        {
                            logger.error("TermsServiceImpl - Terms does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Terms does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        return TermsMapper.mapToTermsResponseDto(terms);
    }

    @Override
    public TermsResponseDto update(Long termsId, TermsRequestDto request) {
        logger.info("TermsServiceImpl - update invoked");
        logger.debug("TermsServiceImpl - termsId: {}", termsId);
        logger.debug("TermsServiceImpl - request: {}", request);
        Terms terms = termsRepository.findById(termsId).orElseThrow(() ->
                {
                    logger.error("TermsServiceImpl - Terms does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Terms does not exist", HttpStatus.NOT_FOUND);
                }
        );
        terms.setName(request.getName());
        terms.setTermsDetailUrl(request.getTermsDetailUrl());
        return TermsMapper.mapToTermsResponseDto(terms);
    }

    @Override
    public void delete(Long termsId) {
        logger.info("TermsServiceImpl - delete invoked");
        logger.debug("TermsServiceImpl - termsId: {}", termsId);
        Terms terms = termsRepository.findById(termsId).orElseThrow(() ->
                {
                    logger.error("TermsServiceImpl - Terms does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Terms does not exist", HttpStatus.NOT_FOUND);
                }
        );
        terms.setIsDeleted(true);
    }

}
