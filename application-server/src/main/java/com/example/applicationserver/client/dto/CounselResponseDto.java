package com.example.applicationserver.client.dto;

import com.example.applicationserver.constants.CommunicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CounselResponseDto {
    private Long counselId;
    private String firstname;
    private String lastname;
    private String cellPhone;
    private String email;
    private String memo;
    private String address;
    private String addressDetail;
    private String zipCode;
    private LocalDateTime appliedAt;
    private CommunicationStatus communicationStatus;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
