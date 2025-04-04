package com.example.judgement_server.client.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ApplicationResponseDto {

    private Long applicationId;

    private String firstname;

    private String lastname;

    private String cellPhone;

    private String email;

    private BigDecimal hopeAmount;

    private BigDecimal approvalAmount;

    private LocalDateTime appliedAt;

    private LocalDateTime contractedAt;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

}
