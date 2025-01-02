package com.example.repayment_server.controller;

import com.example.repayment_server.dto.*;
import com.example.repayment_server.service.IRepaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.repayment_server.dto.ResponseDTO.ok;

@Validated
@RestController
@RequestMapping("/repayments")
@RequiredArgsConstructor
public class RepaymentController {

    private final IRepaymentService repaymentService;

    @PostMapping("/{applicationId}")
    public ResponseDTO<RepaymentResponseDto> createRepayment(
            @PathVariable Long applicationId,
            @Valid @RequestBody RepaymentRequestDto repaymentRequestDto) {
        RepaymentResponseDto responseDto = repaymentService.create(applicationId, repaymentRequestDto);
        return ok(responseDto);
    }

    @GetMapping("/{applicationId}")
    public ResponseDTO<List<RepaymentListResponseDto>> getRepayments(
            @PathVariable Long applicationId) {
        List<RepaymentListResponseDto> responseDtos = repaymentService.get(applicationId);
        return ok(responseDtos);
    }

    @PutMapping("/{repaymentId}")
    public ResponseDTO<RepaymentUpdateResponseDto> updateRepayment(
            @PathVariable Long repaymentId,
            @Valid @RequestBody RepaymentRequestDto repaymentRequestDto) {
        RepaymentUpdateResponseDto responseDto = repaymentService.update(repaymentId, repaymentRequestDto);
        return ok(responseDto);
    }

    @DeleteMapping("/{repaymentId}")
    public ResponseDTO<Void> deleteRepayment(
            @PathVariable Long repaymentId) {
        repaymentService.delete(repaymentId);
        return ok();
    }
}

