package com.example.termsserver.controller;

import com.example.termsserver.dto.ResponseDTO;
import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.service.ITermsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

        ResponseDTO<TermsResponseDto> result = termsController.getAll();

        assertEquals(List.of(response), result.getData());
        verify(termsService, times(1)).getAll();

    }
}