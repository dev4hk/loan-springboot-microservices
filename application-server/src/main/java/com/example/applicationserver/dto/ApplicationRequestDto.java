package com.example.applicationserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(
        name = "Application Request",
        description = "Schema to hold application information"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ApplicationRequestDto {

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
            description = "Cell phone", example = "1234567890"
    )
    @NotEmpty(message = "Cell phone cannot be null or empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Cell phone must be 10 digits")
    private String cellPhone;

    @Schema(
            description = "Email", example = "d2u3y@example.com"
    )
    @NotEmpty(message = "Email cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z0-9._]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;

    @Schema(
            description = "Hope amount", example = "100.00"
    )
    @NotEmpty(message = "Hope amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Hope amount must be a number with up to 2 decimal places")
    private BigDecimal hopeAmount;

}
