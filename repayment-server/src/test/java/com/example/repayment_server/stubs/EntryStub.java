package com.example.repayment_server.stubs;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class EntryStub {

    public void stubGetEntry(Long applicationId) {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":{"
                + "\"entryId\": 1,"
                + "\"applicationId\": " + applicationId + ","
                + "\"entryAmount\": 1000.00,"
                + "\"createdAt\": \"" + LocalDateTime.now() + "\","
                + "\"createdBy\": \"ENTRY_MS\","
                + "\"updatedAt\": \"" + LocalDateTime.now() + "\","
                + "\"updatedBy\": \"ENTRY_MS\""
                + "}"
                + "}";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/entries/" + applicationId))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }
}
