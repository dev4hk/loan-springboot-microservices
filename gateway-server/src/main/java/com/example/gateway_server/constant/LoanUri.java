package com.example.gateway_server.constant;

public enum LoanUri {
    APPLICATION("/loan/application/**"),
    BALANCE("/loan/balance/**"),
    COUNSEL("/loan/counsel/**"),
    ENTRY("/loan/entry/**"),
    FILE_STORAGE("/loan/file-storage/**"),
    JUDGEMENT("/loan/judgement/**"),
    REPAYMENT("/loan/repayment/**"),
    TERMS("/loan/terms/**");

    private final String uri;

    LoanUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
