package com.example.accepttermsserver.controller;

import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.service.IAcceptTermsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AcceptTermsControllerTest {

    @InjectMocks
    AcceptTermsController acceptTermsController;

    @Mock
    IAcceptTermsService acceptTermsService;

    private AcceptTermsRequestDto acceptTermsRequestDto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        acceptTermsRequestDto = AcceptTermsRequestDto.builder()
                .applicationId(1L)
                .termsIds(Arrays.asList(1L))
                .build();
    }

    @Test
    public void createAcceptTerms() {
        doNothing().when(acceptTermsService).create(acceptTermsRequestDto);
        acceptTermsController.create(acceptTermsRequestDto);
        verify(acceptTermsService, times(1)).create(acceptTermsRequestDto);
    }


}