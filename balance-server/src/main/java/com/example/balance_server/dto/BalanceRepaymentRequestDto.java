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
public class BalanceRepaymentRequestDto {

    public enum RepaymentType {
        ADD,
        REMOVE
    }

    @NotNull(message = "Repayment type cannot be null")
    private RepaymentType type;

    @NotNull(message = "Repayment amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Repayment amount must be a number with up to 2 decimal places")
    private BigDecimal repaymentAmount;

}
