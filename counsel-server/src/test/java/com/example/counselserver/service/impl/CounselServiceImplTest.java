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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
                .address1("Somewhere in New York, New York")
                .address2("Random Apartment No. 101, 1st floor No. 101")
                .build();

        CounselRequestDto request = CounselRequestDto.builder()
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("123-456-7890")
                .email("mail@abc.de")
                .memo("I want to get a loan")
                .zipCode("12345")
                .address1("Somewhere in New York, New York")
                .address2("Random Apartment No. 101, 1st floor No. 101")
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

    @Test
    @DisplayName("Get all counsels")
    void should_return_all_counsels_when_get_all_is_called() {
        Pageable pageable = PageRequest.of(0, 5);
        Counsel counsel1 = Counsel.builder()
                .counselId(1L)
                .firstname("John")
                .lastname("Doe")
                .build();
        Counsel counsel2 = Counsel.builder()
                .counselId(2L)
                .firstname("Jane")
                .lastname("Smith")
                .build();

        List<Counsel> counselList = List.of(counsel1, counsel2);
        Page<Counsel> counselPage = new PageImpl<>(counselList, pageable, counselList.size());

        when(counselRepository.findAll(pageable)).thenReturn(counselPage);

        Page<CounselResponseDto> actualPage = counselService.getAll(pageable);

        assertThat(actualPage).isNotNull();
        assertThat(actualPage.getContent()).hasSize(2);
        assertThat(actualPage.getContent().get(0).getCounselId()).isEqualTo(1L);
        assertThat(actualPage.getContent().get(1).getCounselId()).isEqualTo(2L);

        verify(counselRepository, times(1)).findAll(pageable);
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