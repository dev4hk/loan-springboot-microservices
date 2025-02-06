package com.example.judgement_server.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunicationStatus {

    JUDGEMENT_CREATED("JUDGEMENT_CREATED"),
    JUDGEMENT_UPDATED("JUDGEMENT_UPDATED"),
    JUDGEMENT_REMOVED("JUDGEMENT_REMOVED"),
    JUDGEMENT_COMPLETE("JUDGEMENT_COMPLETE");

    private final String type;

}
