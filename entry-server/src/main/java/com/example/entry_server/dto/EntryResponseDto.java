package com.example.entry_server.dto;

import com.example.entry_server.constants.CommunicationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EntryResponseDto {

    private Long entryId;

    private Long applicationId;

    private BigDecimal entryAmount;

    private CommunicationStatus communicationStatus;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;
}
