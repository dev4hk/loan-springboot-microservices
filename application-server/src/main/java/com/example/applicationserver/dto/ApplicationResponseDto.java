package com.example.applicationserver.dto;

import com.example.applicationserver.client.dto.CounselResponseDto;
import com.example.applicationserver.client.dto.FileResponseDto;
import com.example.applicationserver.constants.CommunicationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    private CommunicationStatus communicationStatus;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private CounselResponseDto counselInfo;

    private List<FileResponseDto> fileInfo;

}
