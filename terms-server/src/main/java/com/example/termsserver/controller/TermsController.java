package com.example.termsserver.controller;

import com.example.termsserver.dto.ResponseDTO;
import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.service.ITermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/terms")
@RestController
public class TermsController {

    private final ITermsService termsService;


    public ResponseDTO<TermsResponseDto> create(TermsRequestDto request) {
        return null;
    }

    public ResponseDTO<TermsResponseDto> getAll() {
        return null;
    }
}
