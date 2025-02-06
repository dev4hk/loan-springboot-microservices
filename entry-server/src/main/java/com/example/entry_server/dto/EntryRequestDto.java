package com.example.entry_server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Schema(
        name = "EntryRequestDto",
        description = "Schema to hold entry information"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EntryRequestDto {

    @Schema(
            description = "entry", example = "1000.00"
    )
    @NotNull(message = "Entry amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Entry amount must be a number with up to 2 decimal places")
    @DecimalMin(value = "0.0", inclusive = false, message = "Entry amount must be greater than zero")
    private BigDecimal entryAmount;

}
