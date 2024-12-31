package com.example.applicationserver.client.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FileResponseDto {
    private String name;
    private String url;
}
