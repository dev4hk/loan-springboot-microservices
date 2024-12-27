package com.example.termsserver.controller;

import com.example.termsserver.dto.ResponseDTO;
import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.service.ITermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.termsserver.dto.ResponseDTO.ok;

@RequiredArgsConstructor
@RequestMapping("/terms")
@RestController
public class TermsController {

    private final ITermsService termsService;

    @PostMapping
    public ResponseDTO<TermsResponseDto> create(TermsRequestDto request) {
        return ok(termsService.create(request));
    }

    @GetMapping
    public ResponseDTO<List<TermsResponseDto>> getAll() {
        return ok(termsService.getAll());
    }
}
