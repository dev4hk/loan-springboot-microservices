package com.example.applicationserver.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AcceptTermsRequestDto {

    @NotNull(message = "Application id cannot be null or empty")
    private Long applicationId;

    @NotNull(message = "Terms ids cannot be null or empty")
    private List<@NotNull(message = "Terms id cannot be null or empty") Long> termsIds;

}