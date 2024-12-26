package com.example.counselserver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CounselRequestDto {

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

    @NotEmpty(message = "Memo cannot be null or empty")
    private String memo;

    @NotEmpty(message = "Address cannot be null or empty")
    private String address;

    @NotEmpty(message = "Address detail cannot be null or empty")
    private String addressDetail;

    @NotEmpty(message = "Zip code cannot be null or empty")
    @Pattern(regexp = "^[0-9]{5}$", message = "Zip code must be 5 digits")
    private String zipCode;

}
