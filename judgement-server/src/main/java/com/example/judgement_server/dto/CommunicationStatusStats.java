package com.example.judgement_server.dto;

import com.example.judgement_server.constants.CommunicationStatus;

public interface CommunicationStatusStats {
    CommunicationStatus getCommunicationStatus();
    Long getCount();
}
