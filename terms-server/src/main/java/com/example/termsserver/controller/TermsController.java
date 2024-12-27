package com.example.termsserver.controller;

import com.example.termsserver.dto.ResponseDTO;
import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.service.ITermsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.termsserver.dto.ResponseDTO.ok;

@Validated
@RequiredArgsConstructor
@RequestMapping("/terms")
@RestController
public class TermsController {

    private final ITermsService termsService;

    @PostMapping
    public ResponseDTO<TermsResponseDto> create(@Valid @RequestBody TermsRequestDto request) {
        return ok(termsService.create(request));
    }

    @GetMapping
    public ResponseDTO<List<TermsResponseDto>> getAll() {
        return ok(termsService.getAll());
    }
}
