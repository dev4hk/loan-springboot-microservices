package com.example.judgement_server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Schema(
        name = "Judgement Request",
        description = "Schema to hold judgement information"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class JudgementRequestDto {

    @Schema(
            description = "Application ID", example = "1L"
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
    @NotNull(message = "Hope amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Hope amount must be a number with up to 2 decimal places")
    private BigDecimal approvalAmount;

}
