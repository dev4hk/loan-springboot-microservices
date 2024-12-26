package com.example.applicationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ApplicationRequestDto {

    private String firstname;

    private String lastname;

    private String cellPhone;

    private String email;

    private BigDecimal hopeAmount;

}
