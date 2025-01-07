package com.example.entry_server.service.impl;

import com.example.entry_server.client.ApplicationClient;
import com.example.entry_server.client.BalanceClient;
import com.example.entry_server.client.dto.ApplicationResponseDto;
import com.example.entry_server.client.dto.BalanceRequestDto;
import com.example.entry_server.client.dto.BalanceResponseDto;
import com.example.entry_server.client.dto.BalanceUpdateRequestDto;
import com.example.entry_server.constants.ResultType;
import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.dto.EntryResponseDto;
import com.example.entry_server.dto.EntryUpdateResponseDto;
import com.example.entry_server.dto.ResponseDTO;
import com.example.entry_server.entity.Entry;
import com.example.entry_server.exception.BaseException;
import com.example.entry_server.mapper.EntryMapper;
import com.example.entry_server.repository.EntryRepository;
import com.example.entry_server.service.IEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@RequiredArgsConstructor
@Service
public class EntryServiceImpl implements IEntryService {

    private final EntryRepository entryRepository;
    private final ApplicationClient applicationClient;
    private final BalanceClient balanceClient;

    @Override
    public EntryResponseDto create(Long applicationId, EntryRequestDto request) {

        if (!isContractedApplication(applicationId)) {
            throw new BaseException(ResultType.BAD_REQUEST, "Application is not contracted", HttpStatus.BAD_REQUEST);
        }

        Entry entry = EntryMapper.mapToEntry(request);
        entry.setApplicationId(applicationId);

        Entry created = entryRepository.save(entry);
        ResponseDTO<BalanceResponseDto> balanceResponseDto = balanceClient.create(applicationId,
                BalanceRequestDto.builder()
                        .applicationId(applicationId)
                        .entryAmount(request.getEntryAmount())
                        .build()
        );
        if (balanceResponseDto.getResult().code.equals(ResultType.SYSTEM_ERROR.getCode())) {
            throw new BaseException(ResultType.SYSTEM_ERROR, balanceResponseDto.getResult().desc, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return EntryMapper.mapToEntryResponseDto(created);

    }

    private boolean isContractedApplication(Long applicationId) {
        ResponseDTO<ApplicationResponseDto> responseDto = applicationClient.get(applicationId);
        if (responseDto.getResult().code.equals(ResultType.SYSTEM_ERROR.getCode())) {
            throw new BaseException(ResultType.SYSTEM_ERROR, responseDto.getResult().desc, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (responseDto.getData() == null) {
            throw new BaseException(ResultType.BAD_REQUEST, "Application does not exist", HttpStatus.BAD_REQUEST);
        }
        return responseDto.getData().getContractedAt() != null;
    }

    @Transactional(readOnly = true)
    @Override
    public EntryResponseDto get(Long applicationId) {
        Entry entry = entryRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, "Entry does not exist", HttpStatus.NOT_FOUND));
        return EntryMapper.mapToEntryResponseDto(entry);
    }

    @Override
    public EntryUpdateResponseDto update(Long entryId, EntryRequestDto request) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, "Entry does not exist", HttpStatus.NOT_FOUND));
        BigDecimal beforeEntryAmount = entry.getEntryAmount();
        entry.setEntryAmount(request.getEntryAmount());
        Long applicationId = entry.getApplicationId();

        ResponseDTO<BalanceResponseDto> balanceResponseDto = balanceClient.update(applicationId,
                BalanceUpdateRequestDto.builder()
                        .applicationId(applicationId)
                        .beforeEntryAmount(beforeEntryAmount)
                        .afterEntryAmount(request.getEntryAmount())
                        .build()
        );

        if (balanceResponseDto.getResult().code.equals(ResultType.SYSTEM_ERROR.getCode())) {
            throw new BaseException(ResultType.SYSTEM_ERROR, balanceResponseDto.getResult().desc, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return EntryUpdateResponseDto.builder()
                .entryId(entryId)
                .applicationId(applicationId)
                .beforeEntryAmount(beforeEntryAmount)
                .afterEntryAmount(request.getEntryAmount())
                .build();
    }

    @Override
    public void delete(Long entryId) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new BaseException(ResultType.RESOURCE_NOT_FOUND, "entry does not exist", HttpStatus.NOT_FOUND));
        BigDecimal beforeEntryAMount = entry.getEntryAmount();
        Long applicationId = entry.getApplicationId();
        ResponseDTO<BalanceResponseDto> balanceResponseDto = balanceClient.update(
                applicationId,
                BalanceUpdateRequestDto.builder()
                        .applicationId(applicationId)
                        .beforeEntryAmount(beforeEntryAMount)
                        .afterEntryAmount(BigDecimal.ZERO)
                        .build()
        );
        if (balanceResponseDto.getResult().code.equals(ResultType.SYSTEM_ERROR.getCode())) {
            throw new BaseException(ResultType.SYSTEM_ERROR, balanceResponseDto.getResult().desc, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        entry.setIsDeleted(true);
    }
}
