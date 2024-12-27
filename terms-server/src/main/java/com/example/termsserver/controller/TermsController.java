package com.example.termsserver.controller;

import com.example.termsserver.dto.ResponseDTO;
import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.service.ITermsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.termsserver.dto.ResponseDTO.ok;

@Tag(
        name = "CRUD REST APIs for Terms",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE counsel details"
)
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

    @GetMapping("/{termsId}")
    public ResponseDTO<TermsResponseDto> get(@PathVariable Long termsId) {
        return ok(termsService.get(termsId));
    }

    @PutMapping("/{termsId}")
    public ResponseDTO<TermsResponseDto> update(@PathVariable Long termsId, @Valid @RequestBody TermsRequestDto request) {
        return ok(termsService.update(termsId, request));
    }

    @DeleteMapping("/{termsId}")
    public ResponseDTO<TermsResponseDto> delete(@PathVariable Long termsId) {
        termsService.delete(termsId);
        return ok();
    }
}
