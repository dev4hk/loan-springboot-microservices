package com.example.entry_server.mapper;

import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.dto.EntryResponseDto;
import com.example.entry_server.entity.Entry;

public class EntryMapper {

    public static Entry mapToEntry(EntryRequestDto requestDto) {
        return Entry.builder()
                .entryAmount(requestDto.getEntryAmount())
                .build();
    }

    public static EntryResponseDto mapToEntryResponseDto(Entry entry) {
        return EntryResponseDto.builder()
                .entryId(entry.getEntryId())
                .applicationId(entry.getApplicationId())
                .entryAmount(entry.getEntryAmount())
                .communicationStatus(entry.getCommunicationStatus())
                .createdAt(entry.getCreatedAt())
                .createdBy(entry.getCreatedBy())
                .updatedAt(entry.getUpdatedAt())
                .updatedBy(entry.getUpdatedBy())
                .build();
    }
}
