package com.example.applicationserver.stubs;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@UtilityClass
public class FileStorageStub {

    public void stubUploadFile(Long applicationId, MultipartFile file) {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":null"
                + "}";

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/" + applicationId))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }

    public void stubDownloadFile(Long applicationId, String fileName) {
        String fileContent = "Sample file content";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/" + applicationId + "?fileName=" + fileName))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/octet-stream")
                        .withBody(fileContent.getBytes())));
    }

    public void stubGetFilesInfo(Long applicationId) {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":[{"
                + "\"name\":\"sample.txt\","
                + "\"url\":\"/files/" + applicationId + "/sample.txt\""
                + "}]"
                + "}";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/" + applicationId + "/info"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }

    public void stubDeleteAllFiles(Long applicationId) {
        String jsonResponse = "{"
                + "\"result\":{"
                + "\"code\":\"0200\","
                + "\"message\":\"success\""
                + "},"
                + "\"data\":null"
                + "}";

        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo("/api/" + applicationId))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));
    }
}
