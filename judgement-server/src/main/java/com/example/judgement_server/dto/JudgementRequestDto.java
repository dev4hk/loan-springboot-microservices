package com.example.judgement_server.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class JudgementRequestDto {

    @NotNull(message = "ApplicationId is required")
    private Long applicationId;

    @NotEmpty(message = "First name cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    private String firstname;

    @NotEmpty(message = "Last name cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    private String lastname;

    @NotNull(message = "Hope amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Hope amount must be a number with up to 2 decimal places")
    private BigDecimal approvalAmount;

}
