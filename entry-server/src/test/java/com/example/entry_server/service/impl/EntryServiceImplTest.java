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
import com.example.entry_server.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntryServiceImplTest {

    @InjectMocks
    EntryServiceImpl entryService;

    @Mock
    EntryRepository entryRepository;

    @Mock
    ApplicationClient applicationClient;

    @Mock
    BalanceClient balanceClient;

    EntryRequestDto entryRequestDto;
    Entry entry;
    ApplicationResponseDto applicationResponseDto;


    @BeforeEach
    void setup() {

        entryRequestDto = EntryRequestDto.builder()
                .entryAmount(BigDecimal.valueOf(1000))
                .build();

        entry = Entry.builder()
                .entryId(1L)
                .applicationId(1L)
                .entryAmount(BigDecimal.valueOf(1000))
                .build();

        applicationResponseDto = ApplicationResponseDto.builder()
                .applicationId(1L)
                .firstname("firstname")
                .lastname("lastname")
                .hopeAmount(BigDecimal.valueOf(1000))
                .approvalAmount(BigDecimal.valueOf(1000))
                .contractedAt(LocalDateTime.now())
                .build();

    }

    @DisplayName("Create Entry")
    @Test
    void should_create_entry() {
        Long applicationId = 1L;
        when(applicationClient.get(applicationId)).thenReturn(new ResponseDTO<>(applicationResponseDto));
        when(entryRepository.save(any(Entry.class))).thenReturn(entry);
        when(balanceClient.create(eq(applicationId), any(BalanceRequestDto.class))).thenReturn(
                new ResponseDTO<>(new BalanceResponseDto())
        );


        EntryResponseDto responseDto = entryService.create(applicationId, entryRequestDto);

        assertNotNull(responseDto);
        assertEquals(entryRequestDto.getEntryAmount(), responseDto.getEntryAmount());

    }

    @DisplayName("Create Entry throws error for application not being contracted")
    void should_throw_error_when_create_entry_with_application_not_contracted() {
        Long applicationId = 1L;
        applicationResponseDto.setContractedAt(null);
        when(applicationClient.get(applicationId)).thenReturn(new ResponseDTO<>(applicationResponseDto));
        assertThrows(BaseException.class, () -> entryService.create(applicationId, entryRequestDto));
    }

    @DisplayName("Get Entry")
    @Test
    void should_get_entry() {
        Long applicationId = 1L;
        when(entryRepository.findByApplicationId(applicationId)).thenReturn(Optional.of(entry));
        EntryResponseDto responseDto = entryService.get(applicationId);
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getEntryId());
    }

    @DisplayName("Get null when getting entry that does not exist")
    @Test
    void should_throw_exception_when_getting_entry_not_exist() {
        Long applicationId = 1L;
        when(entryRepository.findByApplicationId(applicationId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertThrows(BaseException.class, () -> entryService.get(applicationId));
    }

    @DisplayName("Update entry")
    @Test
    void should_update_entry() {
        Long entryId = 1L;
        BalanceUpdateRequestDto balanceUpdateRequestDto = BalanceUpdateRequestDto.builder()
                .applicationId(1L)
                .afterEntryAmount(BigDecimal.valueOf(1000))
                .afterEntryAmount(BigDecimal.valueOf(2000))
                .build();

        when(entryRepository.findById(entryId)).thenReturn(Optional.of(entry));
        when(balanceClient.update(1L, balanceUpdateRequestDto)).thenReturn(
                new ResponseDTO<>(BalanceResponseDto.builder()
                        .balanceId(1L)
                        .applicationId(1L)
                        .balance(BigDecimal.valueOf(1000))
                        .build())
        );

        EntryUpdateResponseDto responseDto = entryService.update(entryId, entryRequestDto);

        assertEquals(1L, responseDto.getEntryId());
    }

    @DisplayName("Update entry throws exception when entry does not exist")
    @Test
    void should_throw_exception_when_update_entry_that_not_exist() {
        Long entryId = 1L;
        BalanceUpdateRequestDto balanceUpdateRequestDto = BalanceUpdateRequestDto.builder()
                .applicationId(1L)
                .afterEntryAmount(BigDecimal.valueOf(1000))
                .afterEntryAmount(BigDecimal.valueOf(2000))
                .build();
        when(entryRepository.findById(entryId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));

        assertThrows(BaseException.class, () -> entryService.update(entryId, entryRequestDto));
    }


}