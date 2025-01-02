package com.example.repayment_server.mapper;

import com.example.repayment_server.dto.RepaymentListResponseDto;
import com.example.repayment_server.dto.RepaymentRequestDto;
import com.example.repayment_server.dto.RepaymentResponseDto;
import com.example.repayment_server.entity.Repayment;

public class RepaymentMapper {
    public static Repayment mapToRepayment(RepaymentRequestDto repaymentRequestDto) {
        return Repayment.builder()
                .repaymentAmount(repaymentRequestDto.getRepaymentAmount())
                .build();
    }

    public static RepaymentResponseDto mapToRepaymentResponseDto(Repayment repayment) {
        return RepaymentResponseDto.builder()
                .repaymentId(repayment.getRepaymentId())
                .applicationId(repayment.getApplicationId())
                .repaymentAmount(repayment.getRepaymentAmount())
                .createdAt(repayment.getCreatedAt())
                .createdBy(repayment.getCreatedBy())
                .updatedAt(repayment.getUpdatedAt())
                .updatedBy(repayment.getUpdatedBy())
                .build();
    }

    public static RepaymentListResponseDto mapToRepaymentListResponseDto(Repayment repayment) {
        return RepaymentListResponseDto.builder()
                .repaymentId(repayment.getRepaymentId())
                .repaymentAmount(repayment.getRepaymentAmount())
                .createdAt(repayment.getCreatedAt())
                .createdBy(repayment.getCreatedBy())
                .updatedAt(repayment.getUpdatedAt())
                .updatedBy(repayment.getUpdatedBy())
                .build();
    }
}
