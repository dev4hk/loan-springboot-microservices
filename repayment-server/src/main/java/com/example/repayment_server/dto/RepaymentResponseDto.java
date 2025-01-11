package com.example.repayment_server.dto;

import com.example.repayment_server.constants.CommunicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class RepaymentResponseDto {
    private Long repaymentId;
    private Long applicationId;
    private BigDecimal repaymentAmount;
    private BigDecimal balance;
    private CommunicationStatus communicationStatus;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}