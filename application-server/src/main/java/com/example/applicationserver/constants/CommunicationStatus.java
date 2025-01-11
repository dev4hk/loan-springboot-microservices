package com.example.applicationserver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunicationStatus {

    APPLICATION_RECEIVED("APPLICATION_RECEIVED"),
    APPLICATION_UPDATED("APPLICATION_UPDATED"),
    APPLICATION_REMOVED("APPLICATION_REMOVED"),
    APPLICATION_GRANT_UPDATED("APPLICATION_GRANT_UPDATED"),
    APPLICATION_CONTRACTED("APPLICATION_CONTRACTED");

    private final String type;

}
