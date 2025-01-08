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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CounselServiceImpl implements ICounselService {

    private static final Logger logger = LoggerFactory.getLogger(CounselServiceImpl.class);
    private final CounselRepository counselRepository;

    @Override
    public CounselResponseDto create(CounselRequestDto request) {
        logger.info("CounselServiceImpl - create invoked");
        Counsel counsel = CounselMapper.mapToCounsel(request);
        counsel.setAppliedAt(LocalDateTime.now());
        counsel.setIsDeleted(false);
        Counsel created = counselRepository.save(counsel);
        return CounselMapper.mapToCounselResponseDto(created);
    }

    @Transactional(readOnly = true)
    @Override
    public CounselResponseDto get(Long counselId) {
        logger.info("CounselServiceImpl - get invoked");
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() ->
                        {
                            logger.error("CounselServiceImpl - Counsel does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Counsel does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        return CounselMapper.mapToCounselResponseDto(counsel);
    }

    @Override
    public CounselResponseDto update(Long counselId, CounselRequestDto request) {
        logger.info("CounselServiceImpl - update invoked");
        Counsel counsel = counselRepository.findById(counselId).orElseThrow(() ->
                {
                    logger.error("CounselServiceImpl - Counsel does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Counsel does not exist", HttpStatus.NOT_FOUND);
                }
        );
        counsel.setFirstname(request.getFirstname());
        counsel.setLastname(request.getLastname());
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
        logger.info("CounselServiceImpl - delete invoked");
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() ->
                {
                    logger.error("CounselServiceImpl - Counsel does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Counsel does not exist", HttpStatus.NOT_FOUND);
                });
        counsel.setIsDeleted(true);
    }
}
