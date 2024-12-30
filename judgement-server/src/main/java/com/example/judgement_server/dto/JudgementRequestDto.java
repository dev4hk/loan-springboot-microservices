package com.example.judgement_server.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class JudgementRequestDto {

    private Long judgmentId;

    private Long applicationId;

    private String firstname;

    private String lastname;

    private BigDecimal approvalAmount;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

}
