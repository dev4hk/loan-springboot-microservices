package com.example.balance_server.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BalanceUpdateRequestDto {

    private Long applicationId;

    private BigDecimal beforeEntryAmount;

    private BigDecimal afterEntryAmount;

}
