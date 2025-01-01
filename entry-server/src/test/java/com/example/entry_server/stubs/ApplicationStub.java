package com.example.entry_server.stubs;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@UtilityClass
public class ApplicationStub {


    public void stubApplicationGetCallContracted(Long applicationId) {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":{"
                + "\"applicationId\":1,"
                + "\"firstname\":\"John\","
                + "\"lastname\":\"Doe\","
                + "\"cellPhone\":\"123-456-7890\","
                + "\"email\":\"john.doe@example.com\","
                + "\"hopeAmount\":5000,"
                + "\"approvalAmount\":5000,"
                + "\"appliedAt\":\"" + LocalDateTime.now() + "\","
                + "\"contractedAt\":\"" + LocalDateTime.now() + "\","
                + "\"createdAt\":\"" + LocalDateTime.now() + "\","
                + "\"createdBy\":\"APPLICATION_MS\","
                + "\"updatedAt\":\"" + LocalDateTime.now() + "\","
                + "\"updatedBy\":\"APPLICATION_MS\""
                + "}"
                + "}";

        stubFor(get(urlEqualTo("/applications/" + applicationId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }


    public void stubApplicationGetCallNotContracted(Long applicationId) {

        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":{"
                + "\"applicationId\":1,"
                + "\"firstname\":\"John\","
                + "\"lastname\":\"Doe\","
                + "\"cellPhone\":\"123-456-7890\","
                + "\"email\":\"john.doe@example.com\","
                + "\"hopeAmount\":5000,"
                + "\"approvalAmount\":null,"
                + "\"appliedAt\":\"" + LocalDateTime.now() + "\","
                + "\"contractedAt\":\"" + "null" + "\","
                + "\"createdAt\":\"" + LocalDateTime.now() + "\","
                + "\"createdBy\":\"APPLICATION_MS\","
                + "\"updatedAt\":\"" + LocalDateTime.now() + "\","
                + "\"updatedBy\":\"APPLICATION_MS\""
                + "}"
                + "}";

        stubFor(get(urlEqualTo("/applications/" + applicationId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }

    public void stubApplicationGetCallNonExistent(Long applicationId) {
        stubFor(get(urlEqualTo("/applications/" + applicationId))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"result\": { \"code\": \"0404\", \"message\": \"Application not found\" }, \"data\": null }")));
    }


}
