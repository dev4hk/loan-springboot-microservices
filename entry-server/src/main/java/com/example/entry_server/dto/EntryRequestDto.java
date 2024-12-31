package com.example.entry_server.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EntryRequestDto {

    private BigDecimal entryAmount;

}
