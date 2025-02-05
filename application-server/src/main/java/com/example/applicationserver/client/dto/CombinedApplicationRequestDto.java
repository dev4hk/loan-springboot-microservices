package com.example.applicationserver.client.dto;

import com.example.applicationserver.dto.ApplicationRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CombinedApplicationRequestDto {

    @Valid
    @NotNull(message = "Application details cannot be null")
    private ApplicationRequestDto applicationRequestDto;

    @Valid
    @NotNull(message = "Terms details cannot be null")
    private AcceptTermsRequestDto acceptTermsRequestDto;
}
