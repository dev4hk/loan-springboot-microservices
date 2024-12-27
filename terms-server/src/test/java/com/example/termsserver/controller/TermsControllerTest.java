package com.example.termsserver.controller;

import com.example.termsserver.dto.ResponseDTO;
import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.service.ITermsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TermsControllerTest {

    @InjectMocks
    private TermsController termsController;

    @Mock
    private ITermsService termsService;

    private TermsRequestDto request;
    private TermsResponseDto response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new TermsRequestDto();
        response = new TermsResponseDto();
    }

    @Test
    public void createTerms() {
        when(termsService.create(any(TermsRequestDto.class))).thenReturn(response);

        ResponseDTO<TermsResponseDto> result = termsController.create(request);

        assertEquals(response, result.getData());
        verify(termsService, times(1)).create(any(TermsRequestDto.class));
    }

    @Test
    public void getAllTerms() {
        when(termsService.getAll()).thenReturn(List.of(response));

        ResponseDTO<List<TermsResponseDto>> result = termsController.getAll();

        assertEquals(List.of(response), result.getData());
        verify(termsService, times(1)).getAll();

    }

    @Test
    public void getTerms() {
        when(termsService.get(any(Long.class))).thenReturn(response);

        ResponseDTO<TermsResponseDto> result = termsController.get(1L);

        assertEquals(response, result.getData());
        verify(termsService, times(1)).get(any(Long.class));
    }

    @Test
    public void updateTerms() {
        when(termsService.update(any(Long.class), any(TermsRequestDto.class))).thenReturn(response);

        ResponseDTO<TermsResponseDto> result = termsController.update(1L, request);

        assertEquals(response, result.getData());
        verify(termsService, times(1)).update(any(Long.class), any(TermsRequestDto.class));
    }

    @Test
    public void deleteTerms() {
        doNothing().when(termsService).delete(any(Long.class));

        ResponseDTO<TermsResponseDto> result = termsController.delete(1L);

        verify(termsService, times(1)).delete(any(Long.class));
    }
}