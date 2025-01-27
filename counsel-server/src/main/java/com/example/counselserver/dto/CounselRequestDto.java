package com.example.counselserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Schema(
        name = "Counsel Request",
        description = "Schema to hold counsel information"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CounselRequestDto {

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
            description = "memo", example = "memo"
    )
    @NotEmpty(message = "Memo cannot be null or empty")
    private String memo;

    @Schema(
            description = "address", example = "123 Main St"
    )
    @NotEmpty(message = "Address cannot be null or empty")
    private String address1;

    @Schema(
            description = "address2", example = "Apt 1"
    )
    private String address2;

    @Schema(
            description = "city", example = "New York"
    )
    @NotEmpty(message = "City cannot be null or empty")
    private String city;

    @Schema(
            description = "state", example = "NY"
    )
    @NotEmpty(message = "State cannot be null or empty")
    private String state;

    @Schema(
            description = "zip code", example = "12345"
    )
    @NotEmpty(message = "Zip code cannot be null or empty")
    @Pattern(regexp = "^[0-9]{5}$", message = "Zip code must be 5 digits")
    private String zipCode;

}
