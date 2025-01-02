package com.example.repayment_server.service.impl;

import com.example.repayment_server.client.ApplicationClient;
import com.example.repayment_server.client.BalanceClient;
import com.example.repayment_server.client.EntryClient;
import com.example.repayment_server.dto.RepaymentListResponseDto;
import com.example.repayment_server.dto.RepaymentRequestDto;
import com.example.repayment_server.dto.RepaymentResponseDto;
import com.example.repayment_server.dto.RepaymentUpdateResponseDto;
import com.example.repayment_server.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class RepaymentServiceImpl {

    private final RepaymentRepository repaymentRepository;
    private final ApplicationClient applicationClient;
    private final BalanceClient balanceClient;
    private final EntryClient entryClient;

    public RepaymentResponseDto create(Long applicationId, RepaymentRequestDto repaymentRequestDto) {
        return null;
    }

    public List<RepaymentListResponseDto> get(Long applicationId) {
        return null;
    }

    public RepaymentUpdateResponseDto update(Long repaymentId, RepaymentRequestDto repaymentRequestDto) {
        return null;
    }

    public void delete(Long repaymentId) {

    }
}
