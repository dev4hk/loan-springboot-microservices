package com.example.applicationserver.dto;

import com.example.applicationserver.constants.CommunicationStatus;

public interface CommunicationStatusStats {
    CommunicationStatus getCommunicationStatus();
    Long getCount();
}

