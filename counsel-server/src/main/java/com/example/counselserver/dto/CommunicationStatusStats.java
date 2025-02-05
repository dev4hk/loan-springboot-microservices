package com.example.counselserver.dto;

import com.example.counselserver.constants.CommunicationStatus;

public interface CommunicationStatusStats {
    CommunicationStatus getCommunicationStatus();
    Long getCount();
}

