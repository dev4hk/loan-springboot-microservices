package com.example.judgement_server.service.impl;

import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;
import com.example.judgement_server.entity.Judgement;
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

    @InjectMocks
    private JudgementServiceImpl judgementService;

    private JudgementRequestDto requestDto;
    private Judgement judgement;
    private JudgementResponseDto responseDto;

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
    }

    @Test
    void testCreate() {
        when(judgementRepository.save(any(Judgement.class))).thenReturn(judgement);

        JudgementResponseDto result = judgementService.create(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getApplicationId());
        verify(judgementRepository, times(1)).save(any(Judgement.class));
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
        when(judgementRepository.findByApplicationId(1L)).thenReturn(Optional.of(judgement));

        JudgementResponseDto result = judgementService.getJudgmentOfApplication(1L);

        assertNotNull(result);
        assertEquals(1L, result.getApplicationId());
        verify(judgementRepository, times(1)).findByApplicationId(1L);
    }

    @Test
    void testUpdate() {
        when(judgementRepository.findById(1L)).thenReturn(Optional.of(judgement));

        JudgementResponseDto result = judgementService.update(1L, requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getApplicationId());
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
