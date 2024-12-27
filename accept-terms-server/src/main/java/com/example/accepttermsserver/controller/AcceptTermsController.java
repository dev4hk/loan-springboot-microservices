package com.example.accepttermsserver.controller;

import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.dto.ResponseDTO;
import com.example.accepttermsserver.service.IAcceptTermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/accept-terms")
@RestController
public class AcceptTermsController {

    private final IAcceptTermsService acceptTermsService;

    @PostMapping
    public ResponseDTO<?> create(AcceptTermsRequestDto acceptTermsRequestDto) {
        acceptTermsService.create(acceptTermsRequestDto);
        return ResponseDTO.ok();
    }
}
