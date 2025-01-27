package com.example.counselserver.dto;

import com.example.counselserver.constants.CommunicationStatus;
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
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private LocalDateTime appliedAt;
    private CommunicationStatus communicationStatus;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
