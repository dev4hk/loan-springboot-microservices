package com.example.entry_server.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunicationStatus {

    ENTRY_CREATED("ENTRY_CREATED"),
    ENTRY_UPDATED("ENTRY_UPDATED"),
    ENTRY_REMOVED("ENTRY_REMOVED");

    private final String type;

}
