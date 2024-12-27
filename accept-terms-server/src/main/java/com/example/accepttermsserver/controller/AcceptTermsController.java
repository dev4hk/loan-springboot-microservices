package com.example.accepttermsserver.controller;

import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.dto.ResponseDTO;
import com.example.accepttermsserver.service.IAcceptTermsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RequestMapping("/accept-terms")
@RestController
public class AcceptTermsController {

    private final IAcceptTermsService acceptTermsService;

    @PostMapping
    public ResponseDTO<?> create(@Valid @RequestBody AcceptTermsRequestDto acceptTermsRequestDto) {
        acceptTermsService.create(acceptTermsRequestDto);
        return ResponseDTO.ok();
    }
}
