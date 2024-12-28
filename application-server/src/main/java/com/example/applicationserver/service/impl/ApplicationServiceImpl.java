package com.example.applicationserver.service.impl;

import com.example.applicationserver.cllient.AcceptTermsClient;
import com.example.applicationserver.cllient.TermsClient;
import com.example.applicationserver.cllient.dto.AcceptTermsRequestDto;
import com.example.applicationserver.cllient.dto.TermsResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.entity.Application;
import com.example.applicationserver.exception.BaseException;
import com.example.applicationserver.mapper.ApplicationMapper;
import com.example.applicationserver.repository.ApplicationRepository;
import com.example.applicationserver.service.IApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements IApplicationService {

    private final ApplicationRepository applicationRepository;
    private final TermsClient termClient;
    private final AcceptTermsClient acceptTermsClient;

    @Override
    public ApplicationResponseDto create(ApplicationRequestDto request) {
        Application application = ApplicationMapper.mapToApplication(request);
        application.setAppliedAt(LocalDateTime.now());
        Application created = applicationRepository.save(application);
        return ApplicationMapper.mapToApplicationResponseDto(created);
    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponseDto get(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        return ApplicationMapper.mapToApplicationResponseDto(application);
    }

    @Override
    public ApplicationResponseDto update(Long applicationId, ApplicationRequestDto request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        application.setFirstname(request.getFirstname());
        application.setLastname(request.getLastname());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        return ApplicationMapper.mapToApplicationResponseDto(application);
    }

    @Override
    public void delete(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        application.setIsDeleted(true);
    }

    @Override
    public void acceptTerms(AcceptTermsRequestDto request) {
        get(request.getApplicationId());
        List<TermsResponseDto> terms = termClient.getAll().getData();
        if(terms.isEmpty()) {
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        List<Long> requestTermsIds = request.getTermsIds();

        if(terms.size() != requestTermsIds.size()) {
            throw new BaseException(ResultType.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }

        List<Long> termsIds = terms.stream().map(TermsResponseDto::getTermsId).toList();

        if(!termsIds.containsAll(requestTermsIds)) {
            throw new BaseException(ResultType.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }

        AcceptTermsRequestDto.builder()
                .termsIds(requestTermsIds)
                .applicationId(request.getApplicationId())
                .build();

        acceptTermsClient.create(request);
    }
}
