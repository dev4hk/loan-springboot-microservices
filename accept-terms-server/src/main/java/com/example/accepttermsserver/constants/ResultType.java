package com.example.accepttermsserver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultType {

    SUCCESS("0200", "success"),
    SYSTEM_ERROR("0500", "system error"),
    RESOURCE_NOT_FOUND("0404", "resource not found"),
    BAD_REQUEST("0400", "bad request"),
    DUPLICATED_ACCEPT_TERMS("0409", "resource already exists");

    private final String code;
    private final String desc;
}