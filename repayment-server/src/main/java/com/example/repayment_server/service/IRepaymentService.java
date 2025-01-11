package com.example.repayment_server.service;

import com.example.repayment_server.constants.CommunicationStatus;
import com.example.repayment_server.dto.RepaymentListResponseDto;
import com.example.repayment_server.dto.RepaymentRequestDto;
import com.example.repayment_server.dto.RepaymentResponseDto;
import com.example.repayment_server.dto.RepaymentUpdateResponseDto;

import java.util.List;

public interface IRepaymentService {

    RepaymentResponseDto create(Long applicationId, RepaymentRequestDto request);

    List<RepaymentListResponseDto> get(Long applicationId);

    RepaymentUpdateResponseDto update(Long repaymentId, RepaymentRequestDto request);

    void delete(Long repaymentId);

    void updateCommunicationStatus(Long repaymentId, CommunicationStatus communicationStatus);

}
