package com.example.entry_server.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BalanceUpdateRequestDto {

    private Long applicationId;

    private BigDecimal beforeEntryAmount;

    private BigDecimal afterEntryAmount;

}
