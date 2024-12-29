package com.example.filestorageserver;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileStorageServerApplicationTests {

    @LocalServerPort
    private int port;

    private static final String TEST_UPLOAD_DIR = "./upload/test";

    @BeforeEach
    void setUp() throws IOException {
        RestAssured.port = port;
        Files.createDirectories(Paths.get(TEST_UPLOAD_DIR));
    }

    @AfterEach
    void tearDown() throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get(TEST_UPLOAD_DIR));
    }

    @Test
    void testUploadFile() throws IOException {
        // Arrange
        String url = "/files/1";
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes()
        );

        // Act
        given()
                .multiPart("file", multipartFile.getOriginalFilename(), multipartFile.getInputStream(), multipartFile.getContentType())
                .when()
                .post(url)
                .then()
                .statusCode(200)
                .body("result.code", equalTo("0200"));

        // Assert
        Path filePath = Paths.get(TEST_UPLOAD_DIR + "/1/test.txt");
        assertTrue(Files.exists(filePath));
        assertEquals("Hello, World!", Files.readString(filePath));
    }

    @Test
    void testDownloadFile() throws IOException {
        // Arrange
        Path dirPath = Paths.get(TEST_UPLOAD_DIR + "/1");
        Files.createDirectories(dirPath);
        Files.writeString(dirPath.resolve("test.txt"), "Hello, World!");

        String url = "/files/1?fileName=test.txt";

        // Act & Assert
        given()
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.txt\"")
                .body(equalTo("Hello, World!"));
    }

    @Test
    void testGetFilesInfo() throws IOException {
        // Arrange
        Path dirPath = Paths.get(TEST_UPLOAD_DIR + "/1");
        Files.createDirectories(dirPath);
        Files.createFile(dirPath.resolve("test1.txt"));
        Files.createFile(dirPath.resolve("test2.txt"));

        String url = "/files/1/info";

        // Act & Assert
        given()
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .body("result.code", equalTo("0200"))
                .body("data.size()", is(2))
                .body("data.name", containsInAnyOrder("test1.txt", "test2.txt"));
    }

    @Test
    void testDeleteAllFiles() throws IOException {
        // Arrange
        Path dirPath = Paths.get(TEST_UPLOAD_DIR + "/1");
        Files.createDirectories(dirPath);
        Files.createFile(dirPath.resolve("test1.txt"));
        Files.createFile(dirPath.resolve("test2.txt"));

        String url = "/files/1";

        // Act
        given()
                .when()
                .delete(url)
                .then()
                .statusCode(200)
                .body("result.code", equalTo("0200"));

        // Assert
        assertFalse(Files.exists(dirPath));
    }
}
