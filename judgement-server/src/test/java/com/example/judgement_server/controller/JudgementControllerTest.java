package com.example.judgement_server.controller;

import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;
import com.example.judgement_server.dto.ResponseDTO;
import com.example.judgement_server.service.IJudgementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class JudgementControllerTest {

    @InjectMocks
    private JudgementController judgementController;

    @Mock
    private IJudgementService judgementService;

    private JudgementRequestDto request;
    private JudgementResponseDto response;
    private GrantAmountDto grantAmountDto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = JudgementRequestDto.builder()
                .applicationId(1L)
                .firstname("JudgeFirstName")
                .lastname("JudgeLastName")
                .approvalAmount(BigDecimal.valueOf(10000))
                .build();

        response = JudgementResponseDto.builder()
                .applicationId(1L)
                .firstname("JudgeFirstName")
                .lastname("JudgeLastName")
                .approvalAmount(BigDecimal.valueOf(10000))
                .build();

        grantAmountDto = GrantAmountDto.builder()
                .applicationId(1L)
                .approvalAmount(BigDecimal.valueOf(10000))
                .build();
    }

    @Test
    public void createJudgment() {
        when(judgementService.create(any(JudgementRequestDto.class))).thenReturn(response);

        ResponseDTO<JudgementResponseDto> result = judgementController.create(request);

        assertEquals(response, result.getData());
        verify(judgementService, times(1)).create(any(JudgementRequestDto.class));
    }

    @Test
    public void getJudgment() {
        when(judgementService.get(anyLong())).thenReturn(response);

        ResponseDTO<JudgementResponseDto> result = judgementController.get(1L);

        assertEquals(response, result.getData());
        verify(judgementService, times(1)).get(anyLong());
    }

    @Test
    public void getJudgmentOfApplication() {
        when(judgementService.getJudgementOfApplication(anyLong())).thenReturn(response);

        ResponseDTO<JudgementResponseDto> result = judgementController.getJudgmentOfApplication(1L);

        assertEquals(response, result.getData());
        verify(judgementService, times(1)).getJudgementOfApplication(anyLong());
    }

    @Test
    public void updateJudgment() {
        when(judgementService.update(anyLong(), any(JudgementRequestDto.class))).thenReturn(response);

        ResponseDTO<JudgementResponseDto> result = judgementController.update(1L, request);

        assertEquals(response, result.getData());
        verify(judgementService, times(1)).update(anyLong(), any(JudgementRequestDto.class));
    }

    @Test
    public void deleteJudgment() {
        doNothing().when(judgementService).delete(anyLong());

        ResponseDTO<Void> result = judgementController.delete(1L);

        verify(judgementService, times(1)).delete(anyLong());
    }

    @Test
    public void grantJudgment() {
        when(judgementService.grant(anyLong())).thenReturn(grantAmountDto);

        ResponseDTO<GrantAmountDto> result = judgementController.grant(1L);

        assertEquals(grantAmountDto, result.getData());
        verify(judgementService, times(1)).grant(anyLong());
    }
}
