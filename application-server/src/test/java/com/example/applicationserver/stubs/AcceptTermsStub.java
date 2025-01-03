package com.example.applicationserver.stubs;

import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class AcceptTermsStub {

    public void stubCreateAcceptTerms(AcceptTermsRequestDto acceptTermsRequestDto) {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":[{"
                + "\"acceptTermsId\":1,"
                + "\"applicationId\":1,"
                + "\"termsId\":1,"
                + "\"createdAt\":\"" + LocalDateTime.now() + "\","
                + "\"createdBy\":\"ACCEPT_TERMS_MS\","
                + "\"updatedAt\":\"" + LocalDateTime.now() + "\","
                + "\"updatedBy\":\"ACCEPT_TERMS_MS\""
                + "}]"
                + "}";

        String requestJson = "{"
                + "\"applicationId\": 1,"
                + "\"termsIds\": [1]"
                + "}";

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/accept-terms"))
                .withRequestBody(WireMock.equalToJson(requestJson))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));


    }

}
