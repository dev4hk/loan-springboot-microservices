package com.example.accepttermsserver.service.impl;

import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.entity.AcceptTerms;
import com.example.accepttermsserver.exception.BaseException;
import com.example.accepttermsserver.repository.AcceptTermsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcceptTermsServiceImplTest {

    @InjectMocks
    private AcceptTermsServiceImpl acceptTermsService;

    @Mock
    private AcceptTermsRepository acceptTermsRepository;

    @DisplayName("Create AcceptTerms")
    @Test
    void Should_ReturnResponseOfNewAcceptTermsEntity_When_RequestAcceptTerms() {
        AcceptTermsRequestDto request = AcceptTermsRequestDto.builder()
                .termsIds(Arrays.asList(1L))
                .applicationId(1L)
                .build();

        when(acceptTermsRepository.existsByApplicationIdAndTermsId(1L, 1L)).thenReturn(false);
        when(acceptTermsRepository.save(any(AcceptTerms.class))).thenReturn(AcceptTerms.builder().build());

        acceptTermsService.create(request);

        verify(acceptTermsRepository, times(1)).save(any(AcceptTerms.class));
        verify(acceptTermsRepository, times(1)).existsByApplicationIdAndTermsId(1L, 1L);
    }

    @DisplayName("Create AcceptTerms with empty termsIds")
    @Test
    void Should_ThrowException_When_RequestAcceptTermsWithEmptyTermsIds() {
        AcceptTermsRequestDto request = AcceptTermsRequestDto.builder()
                .termsIds(Arrays.asList())
                .applicationId(1L)
                .build();

        assertThrows(BaseException.class, () -> acceptTermsService.create(request));
    }

    @DisplayName("Create existing AcceptTerms")
    @Test
    void Should_ThrowException_When_RequestAcceptTermsWithExistingTermsIds() {
        AcceptTermsRequestDto request = AcceptTermsRequestDto.builder()
                .termsIds(Arrays.asList(1L))
                .applicationId(1L)
                .build();
        when(acceptTermsRepository.existsByApplicationIdAndTermsId(1L, 1L)).thenReturn(true);

        assertThrows(BaseException.class, () -> acceptTermsService.create(request));
    }
}




