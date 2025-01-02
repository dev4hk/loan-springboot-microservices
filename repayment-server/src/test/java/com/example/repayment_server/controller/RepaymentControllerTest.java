package com.example.repayment_server.controller;

import com.example.repayment_server.dto.*;
import com.example.repayment_server.service.IRepaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepaymentControllerTest {

    @InjectMocks
    private RepaymentController repaymentController;

    @Mock
    private IRepaymentService repaymentService;

    RepaymentRequestDto repaymentRequestDto;
    RepaymentResponseDto repaymentResponseDto;
    RepaymentListResponseDto repaymentListResponseDto;

    Long applicationId = 1L;
    Long repaymentId = 1L;

    @BeforeEach
    void setup() {

        repaymentRequestDto = RepaymentRequestDto.builder()
                .repaymentAmount(BigDecimal.valueOf(1000))
                .build();

        repaymentResponseDto = RepaymentResponseDto.builder()
                .repaymentAmount(BigDecimal.valueOf(1000))
                .applicationId(1L)
                .repaymentId(1L)
                .build();

        repaymentListResponseDto = RepaymentListResponseDto.builder()
                .repaymentId(1L)
                .repaymentAmount(BigDecimal.valueOf(1000))
                .build();
    }

    @DisplayName("Create repayment")
    @Test
    public void create_repayment() {
        when(repaymentService.create(eq(applicationId), any(RepaymentRequestDto.class))).thenReturn(repaymentResponseDto);
        ResponseDTO<RepaymentResponseDto> response = repaymentController.createRepayment(applicationId, repaymentRequestDto);
        assertEquals(repaymentResponseDto, response.getData());
        verify(repaymentService, times(1)).create(eq(applicationId), eq(repaymentRequestDto));
    }

    @DisplayName("Get repayments")
    @Test
    public void get_repayments() {
        when(repaymentService.get(eq(applicationId))).thenReturn(List.of(repaymentListResponseDto));
        ResponseDTO<List<RepaymentListResponseDto>> response = repaymentController.getRepayments(applicationId);
        assertEquals(1, response.getData().size());
        assertEquals(repaymentListResponseDto, response.getData().getFirst());
        verify(repaymentService, times(1)).get(eq(applicationId));
    }

    @DisplayName("Update repayment")
    @Test
    public void update_repayment() {
        when(repaymentService.update(repaymentId, repaymentRequestDto)).thenReturn(RepaymentUpdateResponseDto.builder()
                .applicationId(1L)
                .beforeRepaymentAmount(BigDecimal.valueOf(1000))
                .afterRepaymentAmount(BigDecimal.valueOf(2000))
                .build()
        );
        ResponseDTO<RepaymentUpdateResponseDto> response = repaymentController.updateRepayment(repaymentId, repaymentRequestDto);
        assertEquals(BigDecimal.valueOf(2000), response.getData().getAfterRepaymentAmount());
        verify(repaymentService, times(1)).update(eq(applicationId), eq(repaymentRequestDto));
    }

    @DisplayName("Delete repayment")
    @Test
    public void delete_repayment() {
        doNothing().when(repaymentService).delete(repaymentId);
        repaymentController.deleteRepayment(repaymentId);
        verify(repaymentService, times(1)).delete(eq(repaymentId));
    }


}