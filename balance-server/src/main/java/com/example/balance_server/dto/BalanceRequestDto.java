package com.example.balance_server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Schema(
        name = "Balance Request",
        description = "Schema to hold balance information"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BalanceRequestDto {

    @Schema(
            description = "Application ID", example = "1"
    )
    @NotNull(message = "Application ID cannot be null")
    private Long applicationId;

    @Schema(
            description = "Entry amount", example = "1000.00"
    )
    @NotNull(message = "Entry amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Entry amount must be a number with up to 2 decimal places")
    @DecimalMin(value = "0.0", inclusive = false, message = "Entry amount must be greater than zero")
    private BigDecimal entryAmount;

}
