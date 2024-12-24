package com.example.counselserver.controller;

import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;
import com.example.counselserver.dto.ResponseDTO;
import com.example.counselserver.service.ICounselService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CounselControllerTest {

    @InjectMocks
    private CounselController counselController;

    @Mock
    private ICounselService counselService;

    private CounselRequestDto request;
    private CounselResponseDto response;
    private Long counselId = 1L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new CounselRequestDto();
        response = new CounselResponseDto();
    }

    @Test
    public void createCounsel() {
        when(counselService.create(any(CounselRequestDto.class))).thenReturn(response);

        ResponseDTO<CounselResponseDto> result = counselController.create(request);

        assertEquals(response, result.getData());
        verify(counselService, times(1)).create(any(CounselRequestDto.class));
    }

    @Test
    public void getCounsel() {
        when(counselService.get(counselId)).thenReturn(response);

        ResponseDTO<CounselResponseDto> result = counselController.get(counselId);

        assertEquals(response, result.getData());
        verify(counselService, times(1)).get(counselId);
    }

    @Test
    public void updateCounsel() {
        when(counselService.update(any(Long.class), any(CounselRequestDto.class))).thenReturn(response);

        ResponseDTO<CounselResponseDto> result = counselController.update(counselId, request);

        assertEquals(response, result.getData());
        verify(counselService, times(1)).update(any(Long.class), any(CounselRequestDto.class));
    }

    @Test
    public void deleteCounsel() {
        doNothing().when(counselService).delete(counselId);

        ResponseDTO<CounselResponseDto> result = counselController.delete(counselId);

        verify(counselService, times(1)).delete(counselId);
    }
}
