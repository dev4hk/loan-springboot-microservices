package com.example.judgement_server.service.impl;

import com.example.judgement_server.client.ApplicationClient;
import com.example.judgement_server.client.dto.ApplicationResponseDto;
import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;
import com.example.judgement_server.dto.ResponseDTO;
import com.example.judgement_server.entity.Judgement;
import com.example.judgement_server.repository.JudgementRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JudgementServiceImplTest {

    @Mock
    private JudgementRepository judgementRepository;

    @Mock
    private ApplicationClient applicationClient;

    @Mock
    StreamBridge streamBridge;

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
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(12))
                .payDay(15)
                .interest(BigDecimal.valueOf(5.00))
                .build();

        judgement = Judgement.builder()
                .judgementId(1L)
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
        doThrow(FeignException.class).when(applicationClient).get(anyLong());

        assertThrows(FeignException.class, () -> {
            judgementService.create(requestDto);
        });

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
    void testGetJudgementOfApplication() {
        when(applicationClient.get(1L)).thenReturn(applicationResponse);
        when(judgementRepository.findByApplicationId(1L)).thenReturn(Optional.of(judgement));

        JudgementResponseDto result = judgementService.getJudgementOfApplication(1L);

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
        when(applicationClient.get(1L)).thenReturn(applicationResponse);
        judgementService.delete(1L);

        assertTrue(judgement.getIsDeleted());
        verify(judgementRepository, times(1)).findById(1L);
    }

    @Test
    void testGrant() {
        when(judgementRepository.findById(1L)).thenReturn(Optional.of(judgement));
        when(applicationClient.get(1L)).thenReturn(applicationResponse);

        GrantAmountDto expectedGrantAmountDto = GrantAmountDto.builder()
                .approvalAmount(BigDecimal.valueOf(10000))
                .applicationId(1L)
                .build();

        GrantAmountDto result = judgementService.grant(1L);

        assertNotNull(result);
        assertEquals(expectedGrantAmountDto.getApplicationId(), result.getApplicationId());
        assertEquals(expectedGrantAmountDto.getApprovalAmount(), result.getApprovalAmount());
        verify(judgementRepository, times(1)).findById(1L);
        verify(applicationClient, times(1)).get(1L);
        verify(applicationClient, times(1)).updateGrant(eq(1L), refEq(expectedGrantAmountDto));
    }

    @Test
    void testGrantApplicationNotFound() {
        when(judgementRepository.findById(1L)).thenReturn(Optional.of(judgement));
        when(applicationClient.get(1L)).thenReturn(applicationResponse);
        GrantAmountDto expectedGrantAmountDto = GrantAmountDto.builder()
                .approvalAmount(BigDecimal.valueOf(10000))
                .applicationId(1L)
                .build();
        doThrow(FeignException.class).when(applicationClient).updateGrant(anyLong(), any(GrantAmountDto.class));
        assertThrows(FeignException.class, () -> {
            judgementService.grant(1L);
        });

    }

    @Test
    void testGetNumberOfPayments_TwoPayments() {
        LocalDateTime startDate = LocalDateTime.of(2025, Month.JANUARY, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, Month.MARCH, 1, 0, 0);
        int payDay = 17;

        int result = judgementService.getNumberOfPayments(startDate, endDate, payDay);

        assertEquals(2, result, "Expected 2 payments: one in January and one in February");
    }

    @Test
    void testGetNumberOfPayments_OnePayment() {
        LocalDateTime startDate = LocalDateTime.of(2025, Month.JANUARY, 26, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, Month.MARCH, 1, 0, 0);
        int payDay = 17;

        int result = judgementService.getNumberOfPayments(startDate, endDate, payDay);

        assertEquals(1, result, "Expected 1 payment: only February 17");
    }

    @Test
    void testGetNumberOfPayments_ZeroPayments() {
        LocalDateTime startDate = LocalDateTime.of(2025, Month.FEBRUARY, 18, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, Month.FEBRUARY, 28, 0, 0);
        int payDay = 17;

        int result = judgementService.getNumberOfPayments(startDate, endDate, payDay);

        assertEquals(0, result, "Expected 0 payments: no 17th in the range");
    }

    @Test
    void testGetNumberOfPayments_MultiplePayments() {
        LocalDateTime startDate = LocalDateTime.of(2025, Month.JANUARY, 5, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, Month.JUNE, 20, 0, 0);
        int payDay = 17;

        int result = judgementService.getNumberOfPayments(startDate, endDate, payDay);

        assertEquals(6, result, "Expected 6 payments from January to May");
    }

    @Test
    void testGetNumberOfPayments_StartOnPayDay() {
        LocalDateTime startDate = LocalDateTime.of(2025, Month.MARCH, 17, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, Month.MAY, 20, 0, 0);
        int payDay = 17;

        int result = judgementService.getNumberOfPayments(startDate, endDate, payDay);

        assertEquals(3, result, "Expected 3 payments: March, April, and May");
    }
}

