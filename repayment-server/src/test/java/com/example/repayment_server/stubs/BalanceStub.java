package com.example.repayment_server.stubs;

import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BalanceStub {

    public void stubUpdateBalance(Long applicationId, BalanceRepaymentRequestDto balanceRepaymentRequestDto) {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":{"
                + "\"balanceId\": 1,"
                + "\"applicationId\": 1,"
                + "\"balance\": 5000.00"
                + "}"
                + "}";

        String requestJson = "{"
                + "\"type\": \"" + balanceRepaymentRequestDto.getType() + "\","
                + "\"repaymentAmount\": " + balanceRepaymentRequestDto.getRepaymentAmount()
                + "}";

        WireMock.stubFor(WireMock.put(WireMock.urlEqualTo("/balances/" + applicationId + "/repayment"))
                .withRequestBody(WireMock.equalToJson(requestJson))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }
}
