package com.example.judgement_server.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GrantAmountDto {

    private Long applicationId;

    private BigDecimal approvalAmount;

}
