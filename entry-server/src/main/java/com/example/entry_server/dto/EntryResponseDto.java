package com.example.entry_server.dto;

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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
