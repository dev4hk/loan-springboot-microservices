package com.example.applicationserver.stubs;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.experimental.UtilityClass;
import java.time.LocalDateTime;

@UtilityClass
public class TermsStub {

    public void stubGetAllTerms() {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":[{"
                + "\"termsId\":1,"
                + "\"name\":\"Sample Terms\","
                + "\"termsDetailUrl\":\"http://example.com/terms\","
                + "\"createdAt\":\"" + LocalDateTime.now() + "\","
                + "\"createdBy\":\"TERMS_MS\","
                + "\"updatedAt\":\"" + LocalDateTime.now() + "\","
                + "\"updatedBy\":\"TERMS_MS\""
                + "}]"
                + "}";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }
}
