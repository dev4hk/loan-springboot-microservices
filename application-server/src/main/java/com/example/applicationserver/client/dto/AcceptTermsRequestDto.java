package com.example.applicationserver.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AcceptTermsRequestDto {

    private Long applicationId;

    @NotNull(message = "Terms ids cannot be null or empty")
    private List<@NotNull(message = "Terms id cannot be null or empty") Long> termsIds;

}