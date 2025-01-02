package com.example.repayment_server.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RepaymentRequestDto {
    private BigDecimal repaymentAmount;
}