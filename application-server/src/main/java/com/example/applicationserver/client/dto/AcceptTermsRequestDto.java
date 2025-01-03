package com.example.applicationserver.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(
        name = "Accept Terms Request",
        description = "Schema to hold accept terms information"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AcceptTermsRequestDto {

    @Schema(
            description = "Application id", example = "1"
    )
    @NotNull(message = "Application id cannot be null or empty")
    private Long applicationId;

    @Schema(
            description = "Terms ids", example = "[1, 2, 3]"
    )
    @NotNull(message = "Terms ids cannot be null or empty")
    private List<@NotNull(message = "Terms id cannot be null or empty") Long> termsIds;

}