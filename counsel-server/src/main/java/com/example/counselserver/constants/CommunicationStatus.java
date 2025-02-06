package com.example.counselserver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunicationStatus {

    COUNSEL_RECEIVED("COUNSEL_RECEIVED"),
    COUNSEL_UPDATED("COUNSEL_UPDATED"),
    COUNSEL_COMPLETE("COUNSEL_COMPLETE"),
    COUNSEL_REMOVED("COUNSEL_REMOVED");

    private final String type;

}
