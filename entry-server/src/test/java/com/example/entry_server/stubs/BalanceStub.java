package com.example.entry_server.stubs;

import com.example.entry_server.client.dto.BalanceRequestDto;
import com.example.entry_server.client.dto.BalanceUpdateRequestDto;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BalanceStub {

    public void stubCreateBalance(Long applicationId, BalanceRequestDto balanceRequestDto) {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":{"
                + "\"balanceId\":1,"
                + "\"applicationId\":1,"
                + "\"entryAmount\":1000.00"
                + "}"
                + "}";

        String requestJson = "{"
                + "\"applicationId\": " + balanceRequestDto.getApplicationId() + ","
                + "\"entryAmount\": " + balanceRequestDto.getEntryAmount()
                + "}";

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/" + applicationId))
                .withRequestBody(WireMock.equalToJson(requestJson))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }

    public void stubUpdateBalance(Long applicationId, BalanceUpdateRequestDto balanceUpdateRequestDto) {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":{"
                + "\"balanceId\":1,"
                + "\"applicationId\":1,"
                + "\"beforeEntryAmount\":1000.00,"
                + "\"afterEntryAmount\":2000.00"
                + "}"
                + "}";

        String requestJson = "{"
                + "\"applicationId\": " + balanceUpdateRequestDto.getApplicationId() + ","
                + "\"beforeEntryAmount\": " + balanceUpdateRequestDto.getBeforeEntryAmount() + ","
                + "\"afterEntryAmount\": " + balanceUpdateRequestDto.getAfterEntryAmount()
                + "}";

        WireMock.stubFor(WireMock.put(WireMock.urlEqualTo("/api/" + applicationId))
                .withRequestBody(WireMock.equalToJson(requestJson))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }
}

