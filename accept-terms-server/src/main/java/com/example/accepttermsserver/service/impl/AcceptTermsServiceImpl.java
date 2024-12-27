package com.example.accepttermsserver.service.impl;

import com.example.accepttermsserver.constants.ResultType;
import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.entity.AcceptTerms;
import com.example.accepttermsserver.exception.BaseException;
import com.example.accepttermsserver.repository.AcceptTermsRepository;
import com.example.accepttermsserver.service.IAcceptTermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AcceptTermsServiceImpl implements IAcceptTermsService {

    private final AcceptTermsRepository acceptTermsRepository;

    @Override
    public void create(AcceptTermsRequestDto acceptTermsRequestDto) {
        List<Long> termsIds = acceptTermsRequestDto.getTermsIds();

        if (termsIds.isEmpty()) {
            throw new BaseException(ResultType.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }

        Collections.sort(termsIds);

        Long applicationId = acceptTermsRequestDto.getApplicationId();


        termsIds.forEach(termsId -> {
            if (acceptTermsRepository.existsByApplicationIdAndTermsId(applicationId, termsId)) {
                throw new BaseException(ResultType.DUPLICATED_ACCEPT_TERMS, HttpStatus.BAD_REQUEST);
            }
            AcceptTerms acceptTerms = AcceptTerms.builder()
                    .applicationId(applicationId)
                    .termsId(termsId)
                    .build();
            acceptTermsRepository.save(acceptTerms);
        });
    }

}
