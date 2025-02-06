package com.example.entry_server.dto;

import com.example.entry_server.constants.CommunicationStatus;

public interface CommunicationStatusStats {
    CommunicationStatus getCommunicationStatus();
    Long getCount();
}

