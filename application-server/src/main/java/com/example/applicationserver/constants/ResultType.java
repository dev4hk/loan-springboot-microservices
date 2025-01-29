package com.example.applicationserver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultType {

    SUCCESS("0200", "success"),
    SYSTEM_ERROR("0500", "system error"),
    RESOURCE_NOT_FOUND("0404", "resource not found"),
    BAD_REQUEST("0400", "bad request"),
    SERVICE_UNAVAILABLE("0503", "service unavailable"),
    CONFLICT("0409", "resource already exists");

    private final String code;
    private final String desc;
}