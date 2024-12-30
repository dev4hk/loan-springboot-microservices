package com.example.judgement_server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Schema(
        name = "Grant Amount",
        description = "Schema to hold grant amount information"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GrantAmountDto {

    @Schema(
            description = "Application Id", example = "1L"
    )
    private Long applicationId;

    @Schema(
            description = "Approval amount", example = "100.00"
    )
    private BigDecimal approvalAmount;

}
