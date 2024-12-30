package com.example.judgement_server.controller;

import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.JudgementResponseDto;
import com.example.judgement_server.dto.ResponseDTO;
import com.example.judgement_server.service.IJudgementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.judgement_server.dto.ResponseDTO.ok;

@RequiredArgsConstructor
@RequestMapping("/judgments")
@RestController
public class JudgementController {

    private final IJudgementService judgementService;

    @PostMapping
    public ResponseDTO<JudgementResponseDto> create(@RequestBody JudgementRequestDto request) {
        return ok(judgementService.create(request));
    }

    @GetMapping("/{judgmentId}")
    public ResponseDTO<JudgementResponseDto> get(@PathVariable Long judgmentId) {
        return ok(judgementService.get(judgmentId));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseDTO<JudgementResponseDto> getJudgmentOfApplication(@PathVariable Long applicationId) {
        return ok(judgementService.getJudgmentOfApplication(applicationId));
    }

    @PutMapping("/{judgmentId}")
    public ResponseDTO<JudgementResponseDto> update(@PathVariable Long judgmentId, @RequestBody JudgementRequestDto request) {
        return ok(judgementService.update(judgmentId, request));
    }

    @DeleteMapping("/{judgmentId}")
    public ResponseDTO<Void> delete(@PathVariable Long judgmentId) {
        judgementService.delete(judgmentId);
        return ok();
    }

    @PatchMapping("/{judgmentId}/grants")
    public ResponseDTO<GrantAmountDto> grant(@PathVariable Long judgmentId) {
        return ok(judgementService.grant(judgmentId));
    }

}
