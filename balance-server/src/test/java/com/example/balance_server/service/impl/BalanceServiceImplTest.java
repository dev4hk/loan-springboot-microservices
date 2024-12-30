package com.example.balance_server.service.impl;

import com.example.balance_server.dto.BalanceRepaymentRequestDto;
import com.example.balance_server.dto.BalanceRequestDto;
import com.example.balance_server.dto.BalanceResponseDto;
import com.example.balance_server.dto.BalanceUpdateRequestDto;
import com.example.balance_server.entity.Balance;
import com.example.balance_server.repository.BalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceImplTest {

    @InjectMocks
    BalanceServiceImpl balanceService;

    @Mock
    BalanceRepository balanceRepository;

    private Balance balance;

    private BalanceRequestDto balanceRequestDto;

    @BeforeEach
    void setup() {

        this.balance = Balance.builder()
                .balanceId(1L)
                .applicationId(1L)
                .balance(BigDecimal.valueOf(1000))
                .build();

        this.balanceRequestDto = BalanceRequestDto.builder()
                .applicationId(1L)
                .entryAmount(BigDecimal.valueOf(500))
                .build();

    }

    @DisplayName("create balance")
    @Test
    void should_create_balance() {
        Long applicationId = 1L;
        when(balanceRepository.findByApplicationId(applicationId)).thenReturn(Optional.empty());
        Balance savedBalance = Balance.builder()
                .balanceId(1L)
                .applicationId(1L)
                .balance(BigDecimal.valueOf(500))
                .build();
        when(balanceRepository.save(any(Balance.class))).thenReturn(savedBalance);

        BalanceRequestDto request = BalanceRequestDto.builder()
                .applicationId(1L)
                .entryAmount(BigDecimal.valueOf(500))
                .build();

        BalanceResponseDto response = balanceService.create(applicationId, request);

        assertNotNull(response);
        assertEquals(applicationId, response.getApplicationId());
        assertEquals(BigDecimal.valueOf(500), response.getBalance());
        verify(balanceRepository, times(1)).save(any(Balance.class));
    }

    @DisplayName("get balance")
    @Test
    void should_get_balance() {
        Long applicationId = 1L;
        when(balanceRepository.findByApplicationId(applicationId)).thenReturn(Optional.of(this.balance));

        BalanceResponseDto response = balanceService.get(applicationId);

        assertNotNull(response);
        assertEquals(applicationId, response.getApplicationId());
        assertEquals(BigDecimal.valueOf(1000), response.getBalance());
    }

    @DisplayName("update balance")
    @Test
    void should_update_balance() {
        Long applicationId = 1L;
        when(balanceRepository.findByApplicationId(applicationId)).thenReturn(Optional.of(this.balance));

        BalanceUpdateRequestDto updateRequest = BalanceUpdateRequestDto.builder()
                .beforeEntryAmount(BigDecimal.valueOf(200))
                .afterEntryAmount(BigDecimal.valueOf(300))
                .build();

        BalanceResponseDto response = balanceService.update(applicationId, updateRequest);

        assertNotNull(response);
        assertEquals(applicationId, response.getApplicationId());
        assertEquals(BigDecimal.valueOf(1100), response.getBalance());
    }

    @DisplayName("repayment update balance")
    @Test
    void should_repayment_update_balance() {
        Long applicationId = 1L;
        when(balanceRepository.findByApplicationId(applicationId)).thenReturn(Optional.of(this.balance));

        BalanceRepaymentRequestDto repaymentRequest = BalanceRepaymentRequestDto.builder()
                .repaymentAmount(BigDecimal.valueOf(100))
                .type(BalanceRepaymentRequestDto.RepaymentType.ADD)
                .build();

        BalanceResponseDto response = balanceService.repaymentUpdate(applicationId, repaymentRequest);

        assertNotNull(response);
        assertEquals(applicationId, response.getApplicationId());
        assertEquals(BigDecimal.valueOf(1100), response.getBalance());

        repaymentRequest.setType(BalanceRepaymentRequestDto.RepaymentType.REMOVE);
        response = balanceService.repaymentUpdate(applicationId, repaymentRequest);

        assertNotNull(response);
        assertEquals(applicationId, response.getApplicationId());
        assertEquals(BigDecimal.valueOf(1000), response.getBalance());
    }

    @DisplayName("delete balance")
    @Test
    void should_delete_balance() {
        Long applicationId = 1L;
        when(balanceRepository.findByApplicationId(applicationId)).thenReturn(Optional.of(this.balance));

        balanceService.delete(applicationId);

        verify(balanceRepository, times(1)).save(any(Balance.class));
        assertTrue(this.balance.getIsDeleted());
    }

}