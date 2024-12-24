package com.example.counselserver.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CounselResponseDto {
    private Long counselId;
    private String name;
    private String cellPhone;
    private String email;
    private String memo;
    private String address;
    private String addressDetail;
    private String zipCode;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
