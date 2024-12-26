package com.example.applicationserver.controller;

import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.service.IApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.applicationserver.dto.ResponseDTO.ok;

@RequiredArgsConstructor
@RequestMapping("/applications")
@RestController
public class ApplicationController {

    private final IApplicationService applicationService;

    @PostMapping
    public ResponseDTO<ApplicationResponseDto> create(@RequestBody ApplicationRequestDto request) {
        return ok(applicationService.create(request));
    }

    @GetMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId) {
        return ok(applicationService.get(applicationId));
    }

    @PutMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> update(@PathVariable Long applicationId, @RequestBody ApplicationRequestDto request) {
        return ok(applicationService.update(applicationId, request));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseDTO<ApplicationResponseDto> delete(@PathVariable Long applicationId) {
        applicationService.delete(applicationId);
        return ok();
    }

}
