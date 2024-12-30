package com.example.applicationserver.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GrantAmountDto {

    @NotNull(message = "Application ID cannot be null")
    private Long applicationId;

    @NotNull(message = "Approval amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Approval amount must be greater than zero")
    @Digits(integer = 15, fraction = 2, message = "Approval amount must be a valid decimal number")
    private BigDecimal approvalAmount;

}
