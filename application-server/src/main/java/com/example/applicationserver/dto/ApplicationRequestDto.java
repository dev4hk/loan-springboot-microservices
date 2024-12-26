package com.example.applicationserver.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ApplicationRequestDto {

    @NotEmpty(message = "First name cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    private String firstname;

    @NotEmpty(message = "Last name cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    private String lastname;

    @NotEmpty(message = "Cell phone cannot be null or empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Cell phone must be 10 digits")
    private String cellPhone;

    @NotEmpty(message = "Email cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z0-9._]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Hope amount cannot be null or empty")
    @Digits(integer = 15, fraction = 2, message = "Hope amount must be a number with up to 2 decimal places")
    private BigDecimal hopeAmount;

}
