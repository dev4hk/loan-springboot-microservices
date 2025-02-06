package com.example.counselserver.service.impl;

import com.example.counselserver.constants.CommunicationStatus;
import com.example.counselserver.constants.ResultType;
import com.example.counselserver.dto.CommunicationStatusStats;
import com.example.counselserver.dto.CounselMsgDto;
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
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CounselServiceImpl implements ICounselService {

    private static final Logger logger = LoggerFactory.getLogger(CounselServiceImpl.class);
    private final CounselRepository counselRepository;
    private final StreamBridge streamBridge;

    @Override
    public CounselResponseDto create(CounselRequestDto request) {
        logger.info("CounselServiceImpl - create invoked");
        Counsel counsel = CounselMapper.mapToCounsel(request);
        counsel.setAppliedAt(LocalDateTime.now());
        counsel.setIsDeleted(false);
        Counsel created = counselRepository.save(counsel);
        sendCommunication(created, CommunicationStatus.COUNSEL_RECEIVED);
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
        counsel.setAddress1(request.getAddress1());
        counsel.setAddress2(request.getAddress2());
        counsel.setCity(request.getCity());
        counsel.setState(request.getState());
        counsel.setZipCode(request.getZipCode());
        sendCommunication(counsel, CommunicationStatus.COUNSEL_UPDATED);
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
        sendCommunication(counsel, CommunicationStatus.COUNSEL_REMOVED);
    }

    private void sendCommunication(Counsel counsel, CommunicationStatus communicationStatus) {
        var counselMsgDto = new CounselMsgDto(
                counsel.getCounselId(),
                counsel.getFirstname(),
                counsel.getLastname(),
                counsel.getCellPhone(),
                counsel.getEmail(),
                communicationStatus
        );
        logger.debug("Sending Communication request for the details: {}", counselMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", counselMsgDto);
        logger.debug("Is the Communication request successfully invoked?: {}", result);
    }

    @Override
    public void updateCommunicationStatus(Long counselId, CommunicationStatus communicationStatus) {
        logger.info("CounselServiceImpl - updateCommunicationStatus invoked");
        Counsel counsel = counselRepository.findById(counselId).orElseThrow(() ->
                {
                    logger.error("CounselServiceImpl - Counsel does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Counsel does not exist", HttpStatus.NOT_FOUND);
                }
        );
        counsel.setCommunicationStatus(communicationStatus);
    }

    @Override
    public CounselResponseDto getByEmail(String email) {
        logger.info("CounselServiceImpl - getByEmail invoked");
        Counsel counsel = counselRepository.findByEmail(email)
                .orElseThrow(() ->
                        {
                            logger.error("CounselServiceImpl - Counsel does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Counsel does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        return CounselMapper.mapToCounselResponseDto(counsel);

    }

    @Override
    public Page<CounselResponseDto> getAll(Pageable pageable) {
        logger.info("CounselServiceImpl - getAll invoked");
        return counselRepository.findAll(pageable)
                .map(CounselMapper::mapToCounselResponseDto);
    }

    @Override
    public void complete(Long counselId) {
        logger.info("CounselServiceImpl - complete invoked");
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() ->
                        {
                            logger.error("CounselServiceImpl - Counsel does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Counsel does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        sendCommunication(counsel, CommunicationStatus.COUNSEL_COMPLETE);
    }

    @Override
    public Map<CommunicationStatus, Long> getCounselStatistics() {
        logger.info("CounselServiceImpl - getCounselStatistics invoked");
        return counselRepository.getCommunicationStatusStats()
                .stream().collect(Collectors.toMap(CommunicationStatusStats::getCommunicationStatus, CommunicationStatusStats::getCount));
    }

    @Override
    public List<CounselResponseDto> getNewCounsels() {
        return counselRepository.getNewCounsels(CommunicationStatus.COUNSEL_RECEIVED, PageRequest.of(0, 5));
    }
}
