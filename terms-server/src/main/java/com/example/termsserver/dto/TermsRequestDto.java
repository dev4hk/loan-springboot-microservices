package com.example.termsserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TermsRequestDto {

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Terms detail URL cannot be empty")
    @Size(max = 255, message = "Terms detail URL must be less than 255 characters")
    private String termsDetailUrl;

}
