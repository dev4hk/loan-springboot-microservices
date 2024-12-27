package com.example.accepttermsserver.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AcceptTermsResponseDto {
    private Long acceptTermsId;
    private Long applicationId;
    private List<Long> termsIds;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
