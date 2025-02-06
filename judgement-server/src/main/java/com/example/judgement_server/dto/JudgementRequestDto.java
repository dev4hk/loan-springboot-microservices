package com.example.judgement_server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
        name = "JudgementRequestDto",
        description = "Schema to hold judgement information"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class JudgementRequestDto {

    @Schema(
            description = "Application ID", example = "1"
    )
    @NotNull(message = "ApplicationId is required")
    private Long applicationId;

    @Schema(
            description = "First name", example = "John"
    )
    @NotEmpty(message = "First name cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    private String firstname;

    @Schema(
            description = "Last name", example = "Doe"
    )
    @NotEmpty(message = "Last name cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    private String lastname;

    @Schema(
            description = "Approval amount", example = "100.00"
    )
    @NotNull(message = "Approval amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Approval amount must be a number with up to 2 decimal places")
    @DecimalMin(value = "0.0", inclusive = false, message = "Approval amount must be greater than zero")
    private BigDecimal approvalAmount;

    @Schema(
            description = "Loan start date", example = "2025-03-01T00:00:00"
    )
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @Schema(
            description = "Loan end date", example = "2030-03-01T00:00:00"
    )
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @Schema(
            description = "Pay day (day of month for repayment)", example = "15"
    )
    @NotNull(message = "Pay day is required")
    @Min(value = 1, message = "Pay day must be at least 1")
    @Max(value = 31, message = "Pay day must be at most 31")
    private Integer payDay;

    @Schema(
            description = "Interest rate percentage", example = "5.50"
    )
    @NotNull(message = "Interest rate is required")
    @Digits(integer = 3, fraction = 2, message = "Interest must be a valid percentage with up to 2 decimal places")
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate cannot be negative")
    private BigDecimal interest;
}