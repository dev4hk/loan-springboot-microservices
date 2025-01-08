package com.example.balance_server.controller;

import com.example.balance_server.dto.*;
import com.example.balance_server.service.IBalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceControllerTest {

    @InjectMocks
    private BalanceController balanceController;

    @Mock
    private IBalanceService balanceService;

    private BalanceRequestDto request;
    private BalanceResponseDto response;
    private BalanceUpdateRequestDto updateRequest;
    private BalanceRepaymentRequestDto repaymentRequest;
    private Long applicationId = 1L;

    @BeforeEach
    public void setup() {
        request = new BalanceRequestDto();
        response = new BalanceResponseDto();
        updateRequest = new BalanceUpdateRequestDto();
        repaymentRequest = new BalanceRepaymentRequestDto();
    }

    @Test
    public void createBalance() {
        when(balanceService.create(eq(applicationId), any(BalanceRequestDto.class))).thenReturn(response);

        ResponseDTO<BalanceResponseDto> result = balanceController.create(applicationId, request);

        assertEquals(response, result.getData());
        verify(balanceService, times(1)).create(eq(applicationId), any(BalanceRequestDto.class));
    }

    @Test
    public void getBalance() {
        when(balanceService.get(applicationId)).thenReturn(response);

        ResponseDTO<BalanceResponseDto> result = balanceController.get(applicationId);

        assertEquals(response, result.getData());
        verify(balanceService, times(1)).get(applicationId);
    }

    @Test
    public void updateBalance() {
        when(balanceService.update(eq(applicationId), any(BalanceUpdateRequestDto.class))).thenReturn(response);

        ResponseDTO<BalanceResponseDto> result = balanceController.update(applicationId, updateRequest);

        assertEquals(response, result.getData());
        verify(balanceService, times(1)).update(eq(applicationId), any(BalanceUpdateRequestDto.class));
    }

    @Test
    public void repaymentUpdate() {
        when(balanceService.repaymentUpdate(eq(applicationId), anyList())).thenReturn(List.of(response));

        ResponseDTO<List<BalanceResponseDto>> result = balanceController.repaymentUpdate(applicationId, List.of(repaymentRequest));

        assertEquals(response, result.getData().getFirst());
        verify(balanceService, times(1)).repaymentUpdate(eq(applicationId), anyList());
    }

    @Test
    public void deleteBalance() {
        doNothing().when(balanceService).delete(applicationId);

        ResponseDTO<Void> result = balanceController.delete(applicationId);

        assertNull(result.getData());
        verify(balanceService, times(1)).delete(applicationId);
    }
}


