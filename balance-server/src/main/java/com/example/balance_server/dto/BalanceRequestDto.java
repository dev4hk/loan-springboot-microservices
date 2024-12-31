package com.example.balance_server.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BalanceRequestDto {

    @NotNull(message = "Application ID cannot be null")
    private Long applicationId;

    @NotNull(message = "Entry amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Entry amount must be a number with up to 2 decimal places")
    private BigDecimal entryAmount;

}
