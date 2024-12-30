package com.example.judgement_server.service.impl;

import com.example.judgement_server.client.ApplicationClient;
import com.example.judgement_server.client.dto.ApplicationResponseDto;
import com.example.judgement_server.constants.ResultType;
import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;
import com.example.judgement_server.dto.ResponseDTO;
import com.example.judgement_server.entity.Judgement;
import com.example.judgement_server.exception.BaseException;
import com.example.judgement_server.repository.JudgementRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class JudgementServiceImplTest {

    @Mock
    private JudgementRepository judgementRepository;

    @Mock
    private ApplicationClient applicationClient;

    @InjectMocks
    private JudgementServiceImpl judgementService;

    private JudgementRequestDto requestDto;
    private Judgement judgement;
    private JudgementResponseDto responseDto;
    private ResponseDTO<ApplicationResponseDto> applicationResponse;

    @BeforeEach
    void setUp() {
        requestDto = JudgementRequestDto.builder()
                .applicationId(1L)
                .firstname("JudgeFirstName")
                .lastname("JudgeLastName")
                .approvalAmount(BigDecimal.valueOf(10000))
                .build();

        judgement = Judgement.builder()
                .judgmentId(1L)
                .applicationId(1L)
                .firstname("JudgeFirstName")
                .lastname("JudgeLastName")
                .approvalAmount(BigDecimal.valueOf(10000))
                .build();

        responseDto = JudgementResponseDto.builder()
                .applicationId(1L)
                .firstname("JudgeFirstName")
                .lastname("JudgeLastName")
                .approvalAmount(BigDecimal.valueOf(10000))
                .build();

        ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto();
        applicationResponse = new ResponseDTO<>(applicationResponseDto);
    }

    @Test
    void testCreate() {
        when(applicationClient.get(1L)).thenReturn(applicationResponse);
        when(judgementRepository.save(any(Judgement.class))).thenReturn(judgement);

        JudgementResponseDto result = judgementService.create(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getApplicationId());
        verify(applicationClient, times(1)).get(1L);
        verify(judgementRepository, times(1)).save(any(Judgement.class));
    }

    @Test
    void testCreateApplicationNotFound() {
        when(applicationClient.get(1L)).thenReturn(null);

        BaseException exception = assertThrows(BaseException.class, () -> {
            judgementService.create(requestDto);
        });

        assertEquals(ResultType.RESOURCE_NOT_FOUND.getCode(), exception.getCode());
        verify(applicationClient, times(1)).get(1L);
        verify(judgementRepository, never()).save(any(Judgement.class));
    }

    @Test
    void testGet() {
        when(judgementRepository.findById(1L)).thenReturn(Optional.of(judgement));

        JudgementResponseDto result = judgementService.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getApplicationId());
        verify(judgementRepository, times(1)).findById(1L);
    }

    @Test
    void testGetJudgmentOfApplication() {
        when(applicationClient.get(1L)).thenReturn(applicationResponse);
        when(judgementRepository.findByApplicationId(1L)).thenReturn(Optional.of(judgement));

        JudgementResponseDto result = judgementService.getJudgmentOfApplication(1L);

        assertNotNull(result);
        assertEquals(1L, result.getApplicationId());
        verify(applicationClient, times(1)).get(1L);
        verify(judgementRepository, times(1)).findByApplicationId(1L);
    }

    @Test
    void testUpdate() {
        when(applicationClient.get(1L)).thenReturn(applicationResponse);
        when(judgementRepository.findById(1L)).thenReturn(Optional.of(judgement));

        JudgementResponseDto result = judgementService.update(1L, requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getApplicationId());
        verify(applicationClient, times(1)).get(1L);
        verify(judgementRepository, times(1)).findById(1L);
    }

    @Test
    void testDelete() {
        when(judgementRepository.findById(1L)).thenReturn(Optional.of(judgement));

        judgementService.delete(1L);

        assertTrue(judgement.getIsDeleted());
        verify(judgementRepository, times(1)).findById(1L);
    }
}
