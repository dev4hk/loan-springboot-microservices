package com.example.balance_server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Schema(
        name = "Balance Repayment Request",
        description = "Schema to hold balance repayment information"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BalanceRepaymentRequestDto {

    @Schema(
            description = "Schema to hold type of repayment"
    )
    public enum RepaymentType {
        ADD,
        REMOVE
    }

    @Schema(
            description = "Repayment type"
    )
    @NotNull(message = "Repayment type cannot be null")
    private RepaymentType type;

    @Schema(
            description = "Repayment amount"
    )
    @NotNull(message = "Repayment amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Repayment amount must be a number with up to 2 decimal places")
    private BigDecimal repaymentAmount;

}
