package com.example.repayment_server.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunicationStatus {

    REPAYMENT_CREATED("REPAYMENT_RECEIVED"),
    REPAYMENT_UPDATED("REPAYMENT_UPDATED"),
    REPAYMENT_REMOVED("REPAYMENT_REMOVED");

    private final String type;

}
