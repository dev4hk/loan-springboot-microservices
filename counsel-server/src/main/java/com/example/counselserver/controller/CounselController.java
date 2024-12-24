package com.example.counselserver.controller;

import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;
import com.example.counselserver.dto.ResponseDTO;
import com.example.counselserver.service.ICounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.counselserver.dto.ResponseDTO.ok;

@RequiredArgsConstructor
@RequestMapping("/counsels")
@RestController
public class CounselController {

    private final ICounselService counselService;

    @PostMapping
    public ResponseDTO<CounselResponseDto> create(@RequestBody CounselRequestDto request) {
        return ok(counselService.create(request));
    }

    @GetMapping("/{counselId}")
    public ResponseDTO<CounselResponseDto> get(@PathVariable Long counselId) {
        return ok(counselService.get(counselId));
    }

    @PutMapping("/{counselId}")
    public ResponseDTO<CounselResponseDto> update(@PathVariable Long counselId, @RequestBody CounselRequestDto request) {
        return ok(counselService.update(counselId, request));
    }

    @DeleteMapping("/{counselId}")
    public ResponseDTO<CounselResponseDto> delete(@PathVariable Long counselId) {
        counselService.delete(counselId);
        return ok();
    }
}
