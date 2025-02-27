package com.example.accepttermsserver.service.impl;

import com.example.accepttermsserver.constants.ResultType;
import com.example.accepttermsserver.controller.AcceptTermsController;
import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.dto.AcceptTermsResponseDto;
import com.example.accepttermsserver.entity.AcceptTerms;
import com.example.accepttermsserver.exception.BaseException;
import com.example.accepttermsserver.mapper.AcceptTermsMapper;
import com.example.accepttermsserver.repository.AcceptTermsRepository;
import com.example.accepttermsserver.service.IAcceptTermsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AcceptTermsServiceImpl implements IAcceptTermsService {

    private static final Logger logger = LoggerFactory.getLogger(AcceptTermsServiceImpl.class);
    private final AcceptTermsRepository acceptTermsRepository;

    @Override
    public List<AcceptTermsResponseDto> create(AcceptTermsRequestDto acceptTermsRequestDto) {
        logger.info("AcceptTermsServiceImpl - create invoked");
        List<Long> termsIds = new ArrayList<>(acceptTermsRequestDto.getTermsIds());

        if (termsIds.isEmpty()) {
            logger.error("AcceptTermsServiceImpl - Terms does not exist");
            throw new BaseException(ResultType.BAD_REQUEST, "Terms does not exist", HttpStatus.BAD_REQUEST);
        }

        Collections.sort(termsIds);

        Long applicationId = acceptTermsRequestDto.getApplicationId();

        List<AcceptTermsResponseDto> acceptTermsResponseDtos = new ArrayList<>();
        termsIds.forEach(termsId -> {
            if (acceptTermsRepository.existsByApplicationIdAndTermsId(applicationId, termsId)) {
                logger.error("AcceptTermsServiceImpl - AcceptTerms already exists ");
                throw new BaseException(ResultType.DUPLICATED_ACCEPT_TERMS, "Accept Terms already exists", HttpStatus.BAD_REQUEST);
            }
            AcceptTerms acceptTerms = AcceptTerms.builder()
                    .applicationId(applicationId)
                    .termsId(termsId)
                    .build();
            AcceptTerms created = acceptTermsRepository.save(acceptTerms);
            acceptTermsResponseDtos.add(AcceptTermsMapper.mapToResponseDto(created));
        });

        return acceptTermsResponseDtos;

    }

}
