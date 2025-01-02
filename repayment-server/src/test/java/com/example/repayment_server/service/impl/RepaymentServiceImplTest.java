package com.example.repayment_server.service.impl;

import com.example.repayment_server.client.ApplicationClient;
import com.example.repayment_server.client.BalanceClient;
import com.example.repayment_server.client.EntryClient;
import com.example.repayment_server.client.dto.ApplicationResponseDto;
import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.client.dto.EntryResponseDto;
import com.example.repayment_server.dto.*;
import com.example.repayment_server.entity.Repayment;
import com.example.repayment_server.repository.RepaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepaymentServiceImplTest {

    @InjectMocks
    RepaymentServiceImpl repaymentService;

    @Mock
    RepaymentRepository repaymentRepository;

    @Mock
    ApplicationClient applicationClient;

    @Mock
    BalanceClient balanceClient;

    @Mock
    EntryClient entryClient;

    ApplicationResponseDto applicationResponseDto;

    EntryResponseDto entryResponseDto;

    Repayment repayment;

    BalanceRepaymentRequestDto balanceRepaymentDto;

    BalanceResponseDto balanceResponseDto;

    RepaymentRequestDto repaymentRequestDto;

    @BeforeEach
    void setup() {

        applicationResponseDto = ApplicationResponseDto.builder()
                .applicationId(1L)
                .firstname("firstname")
                .lastname("lastname")
                .hopeAmount(BigDecimal.valueOf(1000))
                .approvalAmount(BigDecimal.valueOf(1000))
                .contractedAt(LocalDateTime.now())
                .build();

        entryResponseDto = EntryResponseDto.builder()
                .entryId(1L)
                .applicationId(1L)
                .entryAmount(BigDecimal.valueOf(1000))
                .build();

        repayment = Repayment.builder()
                .repaymentId(1L)
                .applicationId(1L)
                .repaymentAmount(BigDecimal.valueOf(1000))
                .build();

        repaymentRequestDto = RepaymentRequestDto.builder()
                .repaymentAmount(BigDecimal.valueOf(1000))
                .build();

        balanceRepaymentDto = BalanceRepaymentRequestDto.builder()
                .repaymentAmount(BigDecimal.valueOf(1000))
                .type(BalanceRepaymentRequestDto.RepaymentType.REMOVE)
                .build();

        balanceResponseDto = BalanceResponseDto.builder()
                .applicationId(1L)
                .balanceId(1L)
                .balance(BigDecimal.valueOf(1000))
                .build();

    }

    @DisplayName("Create repayment")
    @Test
    void should_create_repayment() {
        Long applicationId = 1L;
        when(applicationClient.get(applicationId)).thenReturn(new ResponseDTO<>(applicationResponseDto));
        when(entryClient.getEntry(applicationId)).thenReturn(new ResponseDTO<>(entryResponseDto));
        when(repaymentRepository.save(any(Repayment.class))).thenReturn(repayment);
        when(balanceClient.repaymentUpdate(eq(applicationId), any(BalanceRepaymentRequestDto.class))).thenReturn(new ResponseDTO<>(balanceResponseDto));

        RepaymentResponseDto response = repaymentService.create(applicationId, repaymentRequestDto);

        assertEquals(BigDecimal.valueOf(1000), response.getBalance());

    }

    @DisplayName("Get repayment")
    @Test
    void should_get_repayment() {
        Long applicationId = 1L;
        when(repaymentRepository.findAllByApplicationId(applicationId)).thenReturn(List.of(repayment));
        List<RepaymentListResponseDto> response = repaymentService.get(applicationId);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getRepaymentId());
    }

    @DisplayName("Update repayment")
    @Test
    void should_update_repayment() {
        Long repaymentId = 1L;
        when(repaymentRepository.findById(repaymentId)).thenReturn(Optional.of(repayment));
        when(balanceClient.repaymentUpdate(eq(1L), any(BalanceRepaymentRequestDto.class))).thenReturn(new ResponseDTO<>(balanceResponseDto));
        RepaymentUpdateResponseDto response = repaymentService.update(repaymentId, repaymentRequestDto);
        assertEquals(1l, response.getApplicationId());
        assertEquals(balanceResponseDto.getBalance(), response.getBalance());
        assertEquals(repayment.getRepaymentAmount(), response.getBeforeRepaymentAmount());
        assertEquals(repaymentRequestDto.getRepaymentAmount(), response.getAfterRepaymentAmount());
    }

    @DisplayName("Delete repayment")
    @Test
    void should_delete_repayment() {
        Long repaymentId = 1L;
        when(repaymentRepository.findById(repaymentId)).thenReturn(Optional.of(repayment));
        when(balanceClient.repaymentUpdate(eq(repaymentId), any(BalanceRepaymentRequestDto.class))).thenReturn(new ResponseDTO<>(balanceResponseDto));
        repaymentService.delete(repaymentId);
        verify(repaymentRepository, times(1)).findById(repaymentId);
        verify(balanceClient, times(1)).repaymentUpdate(anyLong(), any(BalanceRepaymentRequestDto.class));
        assertEquals(true, repayment.getIsDeleted());
    }

}