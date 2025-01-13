package com.example.counselserver.service.impl;

import com.example.counselserver.constants.ResultType;
import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;
import com.example.counselserver.entity.Counsel;
import com.example.counselserver.exception.BaseException;
import com.example.counselserver.repository.CounselRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CounselServiceImplTest {

    @InjectMocks
    CounselServiceImpl counselService;

    @Mock
    CounselRepository counselRepository;

    @Mock
    StreamBridge streamBridge;

    @DisplayName("Create Counsel")
    @Test
    void Should_ReturnResponseOfNewCounselEntity_When_RequestCounsel() {
        Counsel entity = Counsel.builder()
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("123-456-7890")
                .email("mail@abc.de")
                .memo("I want to get a loan")
                .zipCode("12345")
                .address("Somewhere in New York, New York")
                .addressDetail("Random Apartment No. 101, 1st floor No. 101")
                .build();

        CounselRequestDto request = CounselRequestDto.builder()
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("123-456-7890")
                .email("mail@abc.de")
                .memo("I want to get a loan")
                .zipCode("12345")
                .address("Somewhere in New York, New York")
                .addressDetail("Random Apartment No. 101, 1st floor No. 101")
                .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);

        CounselResponseDto actual = counselService.create(request);

        assertThat(actual.getFirstname()).isSameAs(entity.getFirstname());
    }

    @DisplayName("Get a counsel by id")
    @Test
    void Should_ReturnResponseOfExistCounselEntity_When_RequestExistCounselId() {
        Long counselId = 1L;
        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();
        when(counselRepository.findById(counselId)).thenReturn(Optional.ofNullable(entity));
        CounselResponseDto actual = counselService.get(counselId);
        assertThat(actual.getCounselId()).isSameAs(counselId);
    }

    @DisplayName("Get non-existing counsel by id")
    @Test
    void Should_ThrowException_When_RequestNonExistCounselId() {
        Long counselId = 2L;
        when(counselRepository.findById(counselId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertThrows(BaseException.class, () -> counselService.get(counselId));
    }

    @DisplayName("Update a counsel")
    @Test
    void Should_ReturnUpdatedResponseOfExistCounselEntity_When_RequestUpdateExistCounselInfo() {
        Long counselId = 1L;
        Counsel entity = Counsel.builder()
                .counselId(1L)
                .firstname("firstname1")
                .build();

        CounselRequestDto request = CounselRequestDto.builder()
                .firstname("firstname2")
                .build();

        when(counselRepository.findById(counselId)).thenReturn(Optional.of(entity));

        CounselResponseDto actual = counselService.update(counselId, request);

        assertThat(actual.getCounselId()).isSameAs(counselId);
        assertThat(actual.getFirstname()).isSameAs(request.getFirstname());

    }

    @DisplayName("Update non-existing counsel")
    @Test
    void Should_ThrowException_When_RequestUpdateNonExistCounselInfo() {
        Long counselId = 1L;

        CounselRequestDto request = CounselRequestDto.builder()
                .firstname("update")
                .build();

        when(counselRepository.findById(counselId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertThrows(BaseException.class, () -> counselService.update(counselId, request));

    }

    @DisplayName("Delete a counsel")
    @Test
    void Should_DeleteCounselEntity_When_RequestDeleteExistCounsel() {
        Long counselId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        when(counselRepository.findById(counselId)).thenReturn(Optional.of(entity));

        counselService.delete(counselId);

        assertThat(entity.getIsDeleted()).isSameAs(true);
    }

    @DisplayName("Delete non-existing counsel")
    @Test
    void Should_DeleteCounselEntity_When_RequestDeleteNonExistCounsel() {
        Long counselId = 1L;

        when(counselRepository.findById(counselId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));

        assertThrows(BaseException.class, () -> counselService.delete(counselId));
    }
}