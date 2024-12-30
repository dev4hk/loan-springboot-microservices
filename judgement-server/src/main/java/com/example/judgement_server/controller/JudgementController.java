package com.example.judgement_server.controller;

import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;
import com.example.judgement_server.dto.ResponseDTO;
import com.example.judgement_server.service.IJudgementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.judgement_server.dto.ResponseDTO.ok;

@Validated
@RequiredArgsConstructor
@RequestMapping("/judgements")
@RestController
public class JudgementController {

    private final IJudgementService judgementService;

    @PostMapping
    public ResponseDTO<JudgementResponseDto> create(@Valid @RequestBody JudgementRequestDto request) {
        return ok(judgementService.create(request));
    }

    @GetMapping("/{judgementId}")
    public ResponseDTO<JudgementResponseDto> get(@PathVariable Long judgementId) {
        return ok(judgementService.get(judgementId));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseDTO<JudgementResponseDto> getJudgmentOfApplication(@PathVariable Long applicationId) {
        return ok(judgementService.getJudgementOfApplication(applicationId));
    }

    @PutMapping("/{judgementId}")
    public ResponseDTO<JudgementResponseDto> update(@PathVariable Long judgementId, @Valid @RequestBody JudgementRequestDto request) {
        return ok(judgementService.update(judgementId, request));
    }

    @DeleteMapping("/{judgementId}")
    public ResponseDTO<Void> delete(@PathVariable Long judgementId) {
        judgementService.delete(judgementId);
        return ok();
    }

    @PatchMapping("/{judgementId}/grants")
    public ResponseDTO<GrantAmountDto> grant(@PathVariable Long judgementId) {
        return ok(judgementService.grant(judgementId));
    }

}
