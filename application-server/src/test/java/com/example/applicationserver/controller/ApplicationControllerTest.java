package com.example.applicationserver.controller;

import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.service.IApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationControllerTest {

    @InjectMocks
    private ApplicationController applicationController;

    @Mock
    private IApplicationService applicationService;

    private ApplicationRequestDto request;
    private ApplicationResponseDto response;
    private Long applicationId = 1L;

    @BeforeEach
    public void setup() {
        request = new ApplicationRequestDto();
        response = new ApplicationResponseDto();
    }

    @Test
    public void createApplication() {
        when(applicationService.create(any(ApplicationRequestDto.class))).thenReturn(response);

        ResponseDTO<ApplicationResponseDto> result = applicationController.create(request);

        assertEquals(response, result.getData());
        verify(applicationService, times(1)).create(any(ApplicationRequestDto.class));
    }

    @Test
    public void getApplication() throws TimeoutException {
        when(applicationService.get(applicationId)).thenReturn(response);

        ResponseDTO<ApplicationResponseDto> result = applicationController.get(applicationId);

        assertEquals(response, result.getData());
        verify(applicationService, times(1)).get(applicationId);
    }

    @Test
    public void getApplicationByEmail() throws TimeoutException {
        String email = "email@email.com";
        when(applicationService.getByEmail(email)).thenReturn(response);

        ResponseDTO<ApplicationResponseDto> result = applicationController.getByEmail(email);

        assertEquals(response, result.getData());
        verify(applicationService, times(1)).getByEmail(email);
    }

    @Test
    public void updateApplication() {
        when(applicationService.update(any(Long.class), any(ApplicationRequestDto.class))).thenReturn(response);

        ResponseDTO<ApplicationResponseDto> result = applicationController.update(applicationId, request);

        assertEquals(response, result.getData());
        verify(applicationService, times(1)).update(any(Long.class), any(ApplicationRequestDto.class));
    }

    @Test
    public void deleteApplication() {
        doNothing().when(applicationService).delete(applicationId);

        ResponseDTO<ApplicationResponseDto> result = applicationController.delete(applicationId);

        verify(applicationService, times(1)).delete(applicationId);
    }

}
