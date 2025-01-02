package com.example.repayment_server.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RepaymentUpdateResponseDto {
    private Long applicationId;
    private BigDecimal beforeRepaymentAmount;
    private BigDecimal afterRepaymentAmount;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}