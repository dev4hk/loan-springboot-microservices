package com.example.repayment_server.client.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BalanceRepaymentRequestDto {

    public enum RepaymentType {
        ADD,
        REMOVE
    }

    private RepaymentType type;

    private BigDecimal repaymentAmount;

}
