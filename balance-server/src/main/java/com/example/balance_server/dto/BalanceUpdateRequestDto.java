package com.example.balance_server.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BalanceUpdateRequestDto {

    @NotNull(message = "Application ID cannot be null")
    private Long applicationId;

    @NotNull(message = "Entry amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Before entry amount must be a number with up to 2 decimal places")
    private BigDecimal beforeEntryAmount;

    @NotNull(message = "Entry amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "After entry amount must be a number with up to 2 decimal places")
    private BigDecimal afterEntryAmount;

}
