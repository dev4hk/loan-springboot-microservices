package com.example.message_server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunicationStatus {

    APPLICATION_RECEIVED("APPLICATION_RECEIVED"),
    APPLICATION_UPDATED("APPLICATION_UPDATED"),
    APPLICATION_REMOVED("APPLICATION_REMOVED"),
    APPLICATION_GRANTED("APPLICATION_GRANTED");

    private final String type;

}