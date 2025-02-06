package com.example.filestorageserver.service.impl;

import com.example.filestorageserver.client.ApplicationClient;
import com.example.filestorageserver.client.dto.ApplicationResponseDto;
import com.example.filestorageserver.constants.ResultType;
import com.example.filestorageserver.dto.ResponseDTO;
import com.example.filestorageserver.dto.ResultObject;
import com.example.filestorageserver.exception.BaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceImplTest {

    private static final String UPLOAD_DIR = "./upload/test";

    @Mock(lenient = true)
    private MultipartFile multipartFile;

    @Mock
    private ApplicationClient applicationClient;

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    @BeforeEach
    void setUp() throws Exception {
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");

        Field uploadPathField = FileStorageServiceImpl.class.getDeclaredField("uploadPath");
        uploadPathField.setAccessible(true);
        uploadPathField.set(fileStorageService, UPLOAD_DIR);
        when(applicationClient.get(anyLong())).thenReturn(new ResponseDTO<>(
                ResultObject.builder().build(),
                ApplicationResponseDto.builder().build()
        ));

    }

    @AfterEach
    void tearDown() throws IOException {
        Path uploadDirPath = Paths.get(UPLOAD_DIR);
        if (Files.exists(uploadDirPath)) {
            Files.walk(uploadDirPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (!file.delete()) {
                            System.err.println("Failed to delete file: " + file.getAbsolutePath());
                        }
                    });
        }
    }

    @Test
    void testSaveFile() throws IOException {
        Long applicationId = 1L;
        InputStream inputStream = mock(InputStream.class);
        when(multipartFile.getInputStream()).thenReturn(inputStream);

        fileStorageService.save(applicationId, multipartFile);

        Path expectedPath = Path.of(UPLOAD_DIR + "/" + applicationId + "/test.txt");
        assertTrue(Files.exists(expectedPath));
    }

    @Test
    void testLoadFile() throws Exception {
        Long applicationId = 1L;
        String fileName = "test.txt";
        Path filePath = Path.of(UPLOAD_DIR + "/" + applicationId + "/" + fileName);
        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);

        Resource resource = fileStorageService.load(applicationId, fileName);

        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    @Test
    void testLoadFileNotFound() {
        Long applicationId = 1L;
        String fileName = "nonexistent.txt";

        BaseException exception = assertThrows(BaseException.class, () -> {
            fileStorageService.load(applicationId, fileName);
        });

        assertEquals(ResultType.RESOURCE_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testLoadAllFiles() throws IOException {
        Long applicationId = 1L;
        Path dirPath = Path.of(UPLOAD_DIR + "/" + applicationId);
        Files.createDirectories(dirPath);
        Files.createFile(dirPath.resolve("test1.txt"));
        Files.createFile(dirPath.resolve("test2.txt"));

        Stream<Path> fileStream = fileStorageService.loadAll(applicationId);

        assertNotNull(fileStream);
        assertEquals(2, fileStream.count());
    }

    @Test
    void testDeleteAllFiles() throws IOException {
        Long applicationId = 1L;
        Path dirPath = Path.of(UPLOAD_DIR + "/" + applicationId);
        Files.createDirectories(dirPath);
        Files.createFile(dirPath.resolve("test1.txt"));
        Files.createFile(dirPath.resolve("test2.txt"));

        fileStorageService.deleteAll(applicationId);

        assertFalse(Files.exists(dirPath));
    }

    @Test
    void testDeleteFile() throws IOException {
        Long applicationId = 1L;
        Path dirPath = Path.of(UPLOAD_DIR + "/" + applicationId);
        Files.createDirectories(dirPath);
        Files.createFile(dirPath.resolve("test1.txt"));
        dirPath = Path.of(UPLOAD_DIR + "/" + applicationId + "/" + "test1.txt");
        assertTrue(Files.exists(dirPath));
        fileStorageService.deleteFile(applicationId, "test1.txt");
        assertFalse(Files.exists(dirPath));
    }
}
