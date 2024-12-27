package com.example.termsserver.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TermsRequestDto {

    private String name;

    private String termsDetailUrl;

}
