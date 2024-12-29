package com.example.filestorageserver.controller;

import com.example.filestorageserver.dto.FileResponseDto;
import com.example.filestorageserver.dto.ResponseDTO;
import com.example.filestorageserver.service.IFileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class FileStorageControllerTest {

    @Mock
    private IFileStorageService fileStorageService;

    @InjectMocks
    private FileStorageController fileStorageController;

    @Mock(lenient = true)
    private MultipartFile multipartFile;

    @Mock
    private Resource resource;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
    }

    @Test
    void testUploadFile() {
        Long applicationId = 1L;
        doNothing().when(fileStorageService).save(anyLong(), any(MultipartFile.class));

        ResponseDTO<Void> response = fileStorageController.upload(applicationId, multipartFile);

        assertNotNull(response);
        assertEquals("0200", response.getResult().getCode());
        verify(fileStorageService, times(1)).save(applicationId, multipartFile);
    }

    @Test
    void testDownloadFile() {
        Long applicationId = 1L;
        String fileName = "test.txt";
        when(fileStorageService.load(anyLong(), anyString())).thenReturn(resource);
        when(resource.getFilename()).thenReturn(fileName);

        ResponseEntity<Resource> response = fileStorageController.download(applicationId, fileName);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("attachment; filename=\"" + fileName + "\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(resource, response.getBody());
        verify(fileStorageService, times(1)).load(applicationId, fileName);
    }

    @Test
    void testGetFilesInfo() {
        Long applicationId = 1L;
        Path path1 = mock(Path.class);
        Path path2 = mock(Path.class);
        when(path1.getFileName()).thenReturn(Path.of("test1.txt"));
        when(path2.getFileName()).thenReturn(Path.of("test2.txt"));
        when(fileStorageService.loadAll(anyLong())).thenReturn(Stream.of(path1, path2));

        ResponseDTO<List<FileResponseDto>> response = fileStorageController.getFilesInfo(applicationId);

        assertNotNull(response);
        assertEquals("0200", response.getResult().getCode());
        assertEquals(2, response.getData().size());
        assertEquals("test1.txt", response.getData().get(0).getName());
        assertEquals("test2.txt", response.getData().get(1).getName());
        verify(fileStorageService, times(1)).loadAll(applicationId);
    }

    @Test
    void testDeleteAllFiles() {
        Long applicationId = 1L;
        doNothing().when(fileStorageService).deleteAll(anyLong());

        ResponseDTO<Void> response = fileStorageController.deleteAll(applicationId);

        assertNotNull(response);
        assertEquals("0200", response.getResult().getCode());
        verify(fileStorageService, times(1)).deleteAll(applicationId);
    }
}
