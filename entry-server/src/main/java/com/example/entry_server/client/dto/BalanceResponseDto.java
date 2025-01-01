package com.example.entry_server.client.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BalanceResponseDto {

    private Long balanceId;

    private Long applicationId;

    private BigDecimal balance;

}
