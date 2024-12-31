package com.example.entry_server.service.impl;

import com.example.entry_server.client.ApplicationClient;
import com.example.entry_server.client.BalanceClient;
import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.repository.EntryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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

    @DisplayName("Create Entry")
    @Test
    void should_create_entry() {

    }

}