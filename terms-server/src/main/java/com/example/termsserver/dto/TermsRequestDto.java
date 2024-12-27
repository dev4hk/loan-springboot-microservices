package com.example.termsserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TermsRequestDto {

    @Schema(
            description = "Name", example = "terms"
    )
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Schema(
            description = "Terms detail URL", example = "https://randomTermsPage"
    )
    @NotBlank(message = "Terms detail URL cannot be empty")
    @Size(max = 255, message = "Terms detail URL must be less than 255 characters")
    private String termsDetailUrl;

}
