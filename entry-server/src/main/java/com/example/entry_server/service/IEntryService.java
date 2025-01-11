package com.example.entry_server.service;

import com.example.entry_server.constants.CommunicationStatus;
import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.dto.EntryResponseDto;
import com.example.entry_server.dto.EntryUpdateResponseDto;

public interface IEntryService {

    EntryResponseDto create(Long applicationId, EntryRequestDto request);

    EntryResponseDto get(Long applicationId);

    EntryUpdateResponseDto update(Long entryId, EntryRequestDto request);

    void delete(Long entryId);

    void updateCommunicationStatus(Long applicationId, CommunicationStatus communicationStatus);
}
