package com.example.applicationserver.cllient.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AcceptTermsResponseDto {
    private Long acceptTermsId;
    private Long applicationId;
    private Long termsId;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
