package com.example.counselserver.service.impl;

import com.example.counselserver.constants.ResultType;
import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;
import com.example.counselserver.entity.Counsel;
import com.example.counselserver.exception.BaseException;
import com.example.counselserver.mapper.CounselMapper;
import com.example.counselserver.repository.CounselRepository;
import com.example.counselserver.service.ICounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CounselServiceImpl implements ICounselService {

    private final CounselRepository counselRepository;

    @Override
    public CounselResponseDto create(CounselRequestDto request) {
        Counsel counsel = CounselMapper.mapToCounsel(request);
        counsel.setAppliedAt(LocalDateTime.now());
        counsel.setIsDeleted(false);
        Counsel created = counselRepository.save(counsel);
        return CounselMapper.mapToCounselResponseDto(created);
    }

    @Transactional(readOnly = true)
    @Override
    public CounselResponseDto get(Long counselId) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        return CounselMapper.mapToCounselResponseDto(counsel);
    }

    @Override
    public CounselResponseDto update(Long counselId, CounselRequestDto request) {
        Counsel counsel = counselRepository.findById(counselId).orElseThrow(() ->
                new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        counsel.setName(request.getName());
        counsel.setCellPhone(request.getCellPhone());
        counsel.setEmail(request.getEmail());
        counsel.setMemo(request.getMemo());
        counsel.setAddress(request.getAddress());
        counsel.setAddressDetail(request.getAddressDetail());
        counsel.setZipCode(request.getZipCode());
        return CounselMapper.mapToCounselResponseDto(counsel);
    }

    @Override
    public void delete(Long counselId) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        counsel.setIsDeleted(true);
    }
}
