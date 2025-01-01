package com.example.entry_server.controller;

import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.dto.EntryResponseDto;
import com.example.entry_server.dto.EntryUpdateResponseDto;
import com.example.entry_server.dto.ResponseDTO;
import com.example.entry_server.service.IEntryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntryControllerTest {

    @InjectMocks
    private EntryController entryController;

    @Mock
    private IEntryService entryService;

    private EntryRequestDto request;
    private EntryResponseDto response;
    private EntryUpdateResponseDto updateResponse;

    @BeforeEach
    public void setup() {
        request = EntryRequestDto.builder()
                .entryAmount(BigDecimal.valueOf(1000))
                .build();

        response = EntryResponseDto.builder()
                .applicationId(1L)
                .entryAmount(BigDecimal.valueOf(1000))
                .build();

        updateResponse = EntryUpdateResponseDto.builder()
                .entryId(1L)
                .applicationId(1L)
                .beforeEntryAmount(BigDecimal.valueOf(1000))
                .afterEntryAmount(BigDecimal.valueOf(2000))
                .build();
    }

    @DisplayName("Create Entry")
    @Test
    public void createEntry() {
        when(entryService.create(anyLong(), any(EntryRequestDto.class))).thenReturn(response);

        ResponseDTO<EntryResponseDto> result = entryController.createEntry(1L, request);

        assertEquals(response, result.getData());
        verify(entryService, times(1)).create(anyLong(), any(EntryRequestDto.class));
    }

    @DisplayName("Get Entry")
    @Test
    public void getEntry() {
        when(entryService.get(anyLong())).thenReturn(response);

        ResponseDTO<EntryResponseDto> result = entryController.getEntry(1L);

        assertEquals(response, result.getData());
        verify(entryService, times(1)).get(anyLong());
    }

    @DisplayName("Update Entry")
    @Test
    public void updateEntry() {
        when(entryService.update(anyLong(), any(EntryRequestDto.class))).thenReturn(updateResponse);

        ResponseDTO<EntryUpdateResponseDto> result = entryController.updateEntry(1L, request);

        assertEquals(updateResponse, result.getData());
        verify(entryService, times(1)).update(anyLong(), any(EntryRequestDto.class));
    }

    @DisplayName("Delete Entry")
    @Test
    public void deleteEntry() {
        doNothing().when(entryService).delete(anyLong());

        ResponseDTO<Void> result = entryController.deleteEntry(1L);

        verify(entryService, times(1)).delete(anyLong());
    }
}
