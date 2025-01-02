package com.example.repayment_server.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RepaymentListResponseDto {
    private Long repaymentId;
    private BigDecimal repaymentAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}