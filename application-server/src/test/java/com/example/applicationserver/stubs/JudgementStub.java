package com.example.applicationserver.stubs;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.experimental.UtilityClass;
import java.time.LocalDateTime;

@UtilityClass
public class JudgementStub {

    public void stubGetJudgmentOfApplication(Long applicationId) {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":{"
                + "\"judgementId\":1,"
                + "\"applicationId\":" + applicationId + ","
                + "\"firstname\":\"John\","
                + "\"lastname\":\"Doe\","
                + "\"approvalAmount\":5000.00,"
                + "\"createdAt\":\"" + LocalDateTime.now() + "\","
                + "\"createdBy\":\"JUDGEMENT_MS\","
                + "\"updatedAt\":\"" + LocalDateTime.now() + "\","
                + "\"updatedBy\":\"JUDGEMENT_MS\""
                + "}"
                + "}";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/applications/" + applicationId))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }
}
