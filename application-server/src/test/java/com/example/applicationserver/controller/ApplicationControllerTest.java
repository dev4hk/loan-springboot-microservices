package com.example.applicationserver.controller;

import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
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
import org.springframework.data.domain.*;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private AcceptTermsRequestDto acceptTermsRequestDto;
    private Long applicationId = 1L;

    @BeforeEach
    public void setup() {
        request = new ApplicationRequestDto();
        response = new ApplicationResponseDto();
        acceptTermsRequestDto = new AcceptTermsRequestDto();
    }

    @Test
    public void createApplication() {
        when(applicationService.create(any(ApplicationRequestDto.class), any(AcceptTermsRequestDto.class))).thenReturn(response);

        ResponseDTO<ApplicationResponseDto> result = applicationController.create(request, acceptTermsRequestDto);

        assertEquals(response, result.getData());
        verify(applicationService, times(1)).create(any(ApplicationRequestDto.class), any(AcceptTermsRequestDto.class));
    }

    @Test
    public void getApplication() throws TimeoutException {
        when(applicationService.get(applicationId)).thenReturn(response);

        ResponseDTO<ApplicationResponseDto> result = applicationController.get(applicationId);

        assertEquals(response, result.getData());
        verify(applicationService, times(1)).get(applicationId);
    }

    @Test
    void shouldReturnPaginatedApplications() {
        List<ApplicationResponseDto> applicationList = List.of(
                ApplicationResponseDto.builder()
                        .applicationId(1L)
                        .firstname("John")
                        .lastname("Doe")
                        .email("john.doe@example.com")
                        .build(),
                ApplicationResponseDto.builder()
                        .applicationId(2L)
                        .firstname("Jane")
                        .lastname("Smith")
                        .email("jane.smith@example.com")
                        .build()
        );

        Page<ApplicationResponseDto> applicationPage = new PageImpl<>(applicationList);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("applicationId").ascending());
        when(applicationService.getAll(pageable)).thenReturn(applicationPage);

        ResponseDTO<Page<ApplicationResponseDto>> result = applicationController.getAll(pageable);

        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getTotalElements());

        verify(applicationService, times(1)).getAll(pageable);
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
