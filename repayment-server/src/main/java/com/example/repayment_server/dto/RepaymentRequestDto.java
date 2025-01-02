package com.example.repayment_server.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RepaymentRequestDto {

    @NotNull(message = "Repayment amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Repayment amount must be a number with up to 2 decimal places")
    private BigDecimal repaymentAmount;
}