package com.example.entry_server.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EntryUpdateResponseDto {

    private Long entryId;

    private Long applicationId;

    private BigDecimal beforeEntryAmount;

    private BigDecimal afterEntryAmount;

    private LocalDateTime updatedAt;

    private String updatedBy;

}
