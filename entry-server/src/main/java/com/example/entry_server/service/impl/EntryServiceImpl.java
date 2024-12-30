package com.example.entry_server.service.impl;

import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.dto.EntryResponseDto;
import com.example.entry_server.dto.EntryUpdateResponseDto;
import com.example.entry_server.repository.EntryRepository;
import com.example.entry_server.service.IEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class EntryServiceImpl implements IEntryService {

    private final EntryRepository entryRepository;

    @Override
    public EntryResponseDto create(Long applicationId, EntryRequestDto request) {
        return null;
    }

    @Override
    public EntryResponseDto get(Long applicationId) {
        return null;
    }

    @Override
    public EntryUpdateResponseDto update(Long entryId, EntryRequestDto request) {
        return null;
    }

    @Override
    public void delete(Long entryId) {

    }
}
