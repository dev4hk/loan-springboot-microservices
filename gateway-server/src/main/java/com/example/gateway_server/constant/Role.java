package com.example.gateway_server.constant;

public enum Role {
    CUSTOMER("CUSTOMER"),
    MANAGER("MANAGER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
