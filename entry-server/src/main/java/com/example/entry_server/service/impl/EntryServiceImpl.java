package com.example.entry_server.service.impl;

import com.example.entry_server.client.ApplicationClient;
import com.example.entry_server.client.BalanceClient;
import com.example.entry_server.client.dto.ApplicationResponseDto;
import com.example.entry_server.client.dto.BalanceRequestDto;
import com.example.entry_server.client.dto.BalanceUpdateRequestDto;
import com.example.entry_server.constants.CommunicationStatus;
import com.example.entry_server.constants.ResultType;
import com.example.entry_server.dto.*;
import com.example.entry_server.entity.Entry;
import com.example.entry_server.exception.BaseException;
import com.example.entry_server.mapper.EntryMapper;
import com.example.entry_server.repository.EntryRepository;
import com.example.entry_server.service.IEntryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@RequiredArgsConstructor
@Service
public class EntryServiceImpl implements IEntryService {

    private static final Logger logger = LoggerFactory.getLogger(EntryServiceImpl.class);

    private final EntryRepository entryRepository;
    private final ApplicationClient applicationClient;
    private final BalanceClient balanceClient;
    private final StreamBridge streamBridge;

    @Override
    public EntryResponseDto create(Long applicationId, EntryRequestDto request) {
        logger.info("EntryServiceImpl - create invoked");

        ApplicationResponseDto applicationResponseDto = checkContractAndGetApplication(applicationId);

        Entry entry = EntryMapper.mapToEntry(request);
        entry.setApplicationId(applicationId);

        Entry created = entryRepository.save(entry);
        balanceClient.create(applicationId,
                BalanceRequestDto.builder()
                        .applicationId(applicationId)
                        .entryAmount(request.getEntryAmount())
                        .build()
        );
        sendCommunication(created, applicationResponseDto, CommunicationStatus.ENTRY_CREATED);

        return EntryMapper.mapToEntryResponseDto(created);

    }

    private ApplicationResponseDto checkContractAndGetApplication(Long applicationId) {
        logger.info("EntryServiceImpl - isContractedApplication invoked");
        ResponseDTO<ApplicationResponseDto> responseDto = applicationClient.get(applicationId);
        if(responseDto.getData().getContractedAt() == null) {
            logger.error("EntryServiceImpl - Application is not contracted");
            throw new BaseException(ResultType.BAD_REQUEST, "Application is not contracted", HttpStatus.BAD_REQUEST);
        }
        return responseDto.getData();
    }

    private ApplicationResponseDto getApplication(Long applicationId) {
        ResponseDTO<ApplicationResponseDto> responseDto = applicationClient.get(applicationId);
        return responseDto.getData();
    }

    @Transactional(readOnly = true)
    @Override
    public EntryResponseDto get(Long applicationId) {
        logger.info("EntryServiceImpl - get invoked");
        Entry entry = entryRepository.findByApplicationId(applicationId)
                .orElseThrow(() ->
                        {
                            logger.error("EntryServiceImpl - Entry does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Entry does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        return EntryMapper.mapToEntryResponseDto(entry);
    }

    @Override
    public EntryUpdateResponseDto update(Long entryId, EntryRequestDto request) {
        logger.info("EntryServiceImpl - update invoked");
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() ->
                        {
                            logger.error("EntryServiceImpl - Entry does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Entry does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        BigDecimal beforeEntryAmount = entry.getEntryAmount();
        entry.setEntryAmount(request.getEntryAmount());
        Entry updated = entryRepository.save(entry);
        Long applicationId = entry.getApplicationId();

        ApplicationResponseDto applicationResponseDto = getApplication(applicationId);

        balanceClient.update(applicationId,
                BalanceUpdateRequestDto.builder()
                        .applicationId(applicationId)
                        .beforeEntryAmount(beforeEntryAmount)
                        .afterEntryAmount(request.getEntryAmount())
                        .build()
        );

        sendCommunication(entry, applicationResponseDto, CommunicationStatus.ENTRY_UPDATED);

        return EntryUpdateResponseDto.builder()
                .entryId(entryId)
                .applicationId(applicationId)
                .beforeEntryAmount(beforeEntryAmount)
                .afterEntryAmount(request.getEntryAmount())
                .updatedAt(updated.getUpdatedAt())
                .updatedBy(updated.getUpdatedBy())
                .build();
    }

    @Override
    public void delete(Long entryId) {
        logger.info("EntryServiceImpl - delete invoked");
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() ->
                        {
                            logger.error("EntryServiceImpl - Entry does not exist");
                            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Entry does not exist", HttpStatus.NOT_FOUND);
                        }
                );
        BigDecimal beforeEntryAMount = entry.getEntryAmount();
        Long applicationId = entry.getApplicationId();
        ApplicationResponseDto applicationResponseDto = getApplication(applicationId);
        balanceClient.update(
                applicationId,
                BalanceUpdateRequestDto.builder()
                        .applicationId(applicationId)
                        .beforeEntryAmount(beforeEntryAMount)
                        .afterEntryAmount(BigDecimal.ZERO)
                        .build()
        );
        entry.setIsDeleted(true);
        sendCommunication(entry, applicationResponseDto, CommunicationStatus.ENTRY_CREATED);
    }

    @Override
    public void updateCommunicationStatus(Long entryId, CommunicationStatus communicationStatus) {
        logger.info("EntryServiceImpl - updateCommunicationStatus invoked");

        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> {
                    logger.error("EntryServiceImpl - Entry does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Entry does not exist", HttpStatus.NOT_FOUND);
                });
        entry.setCommunicationStatus(communicationStatus);

    }

    private void sendCommunication(Entry entry, ApplicationResponseDto applicationResponseDto, CommunicationStatus communicationStatus) {
        var entryMsgDto = new EntryMsgDto(
                entry.getEntryId(),
                entry.getApplicationId(),
                entry.getEntryAmount(),
                applicationResponseDto.getFirstname(),
                applicationResponseDto.getLastname(),
                applicationResponseDto.getCellPhone(),
                applicationResponseDto.getEmail(),
                communicationStatus
        );

        logger.debug("Sending Communication request for the details: {}", entryMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", entryMsgDto);
        logger.debug("Is the Communication request successfully invoked?: {}", result);
    }
}
