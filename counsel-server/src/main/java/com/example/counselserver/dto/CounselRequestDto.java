package com.example.counselserver.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CounselRequestDto {
    private String name;
    private String cellPhone;
    private String email;
    private String memo;
    private String address;
    private String addressDetail;
    private String zipCode;
}
