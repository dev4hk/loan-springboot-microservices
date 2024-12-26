package com.example.applicationserver.service.impl;

import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.entity.Application;
import com.example.applicationserver.exception.BaseException;
import com.example.applicationserver.repository.ApplicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @InjectMocks
    ApplicationServiceImpl applicationService;

    @Mock
    ApplicationRepository applicationRepository;

    @DisplayName("Create Application")
    @Test
    void Should_ReturnResponseOfNewApplicationEntity_When_RequestCreateApplication() {
        Application entity = Application.builder()
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("1234567890")
                .email("mail@abcd.efg")
                .hopeAmount(BigDecimal.valueOf(50000))
                .build();

        ApplicationRequestDto request = ApplicationRequestDto.builder()
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("1234567890")
                .email("mail@abcd.efg")
                .hopeAmount(BigDecimal.valueOf(50000))
                .build();

        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(entity);
        ApplicationResponseDto actual = applicationService.create(request);
        assertThat(actual).isNotNull();
        assertThat(actual.getHopeAmount()).isSameAs(entity.getHopeAmount());
        assertThat(actual.getFirstname()).isSameAs(entity.getFirstname());
    }

    @DisplayName("Get An Application by ApplicationId")
    @Test
    void Should_ReturnResponseOfExistApplicationEntity_When_RequestExistApplicationId() {
        Long findId = 1L;
        Application entity = Application.builder()
                .applicationId(1L)
                .build();
        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));
        ApplicationResponseDto actual = applicationService.get(findId);
        assertThat(actual).isNotNull();
        assertThat(actual.getApplicationId()).isSameAs(findId);
    }

    @DisplayName("Get non-existing Application by id")
    @Test
    void Should_ThrowException_When_RequestNonExistApplicationId() {
        Long applicationId = 1L;
        when(applicationRepository.findById(applicationId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertThrows(BaseException.class, () -> applicationService.get(applicationId));
    }

    @DisplayName("Update an Application")
    @Test
    void Should_ReturnUpdatedResponseOfExistApplicationEntity_When_RequestUpdateExistApplicationInfo() {
        Long applicationId = 1L;
        Application entity = Application.builder()
                .applicationId(1L)
                .hopeAmount(BigDecimal.valueOf(50000))
                .build();

        ApplicationRequestDto request = ApplicationRequestDto.builder()
                .hopeAmount(BigDecimal.valueOf(60000))
                .build();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(entity));

        ApplicationResponseDto actual = applicationService.update(applicationId, request);
        assertThat(actual).isNotNull();
        assertThat(actual.getApplicationId()).isSameAs(applicationId);
        assertThat(actual.getHopeAmount()).isSameAs(request.getHopeAmount());

    }

    @DisplayName("Update non-existing Application")
    @Test
    void Should_ReturnUpdatedResponseOfExistApplicationEntity_When_RequestUpdateNonExistApplicationInfo() {
        Long applicationId = 1L;
        ApplicationRequestDto request = ApplicationRequestDto.builder().build();
        when(applicationRepository.findById(applicationId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertThrows(BaseException.class, () -> applicationService.update(applicationId, request));
    }

    @DisplayName("Delete an Application by id")
    @Test
    void Should_DeleteApplicationEntity_When_RequestDeleteExistApplication() {
        Long applicationId = 1L;
        Application entity = Application.builder()
                .applicationId(1L)
                .build();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(entity));
        applicationService.delete(applicationId);
        assertThat(entity.getIsDeleted()).isSameAs(true);
    }

    @DisplayName("Delete non-existing Application")
    @Test
    void Should_DeleteApplicationEntity_When_RequestDeleteNonExistApplication() {
        Long applicationId = 1L;
        when(applicationRepository.findById(applicationId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertThrows(BaseException.class, () -> applicationService.delete(applicationId));
    }

}