package com.example.accepttermsserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AcceptTermsRequestDto {

    private Long applicationId;

    private List<Long> termsIds;

}
