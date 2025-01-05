package com.example.judgement_server.stubs;

import com.example.judgement_server.dto.GrantAmountDto;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@UtilityClass
public class ApplicationStub {

    public void stubApplicationGetCall(Long applicationId) {

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
                + "\"createdAt\":\"" + LocalDateTime.now() + "\","
                + "\"createdBy\":\"APPLICATION_MS\","
                + "\"updatedAt\":\"" + LocalDateTime.now() + "\","
                + "\"updatedBy\":\"APPLICATION_MS\""
                + "}"
                + "}";

        stubFor(get(urlEqualTo("/api/" + applicationId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }

    public void stubUpdateGrantCall(Long applicationId, GrantAmountDto grantAmountDto) {

        String jsonRequest = "{"
                + "\"applicationId\":" + grantAmountDto.getApplicationId() + ","
                + "\"approvalAmount\":" + grantAmountDto.getApprovalAmount()
                + "}";

        stubFor(put(urlEqualTo("/api/" + applicationId + "/grant"))
                .withRequestBody(equalToJson(jsonRequest))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{"
                                + "\"result\":{"
                                + "\"code\":\"0200\","
                                + "\"message\":\"success\""
                                + "},"
                                + "\"data\":null"
                                + "}")));
    }
}
