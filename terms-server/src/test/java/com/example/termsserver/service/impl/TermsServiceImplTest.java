package com.example.termsserver.service.impl;

import com.example.termsserver.dto.TermsRequestDto;
import com.example.termsserver.dto.TermsResponseDto;
import com.example.termsserver.entity.Terms;
import com.example.termsserver.repository.TermsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TermsServiceImplTest {

    @InjectMocks
    private TermsServiceImpl termsService;

    @Mock
    TermsRepository termsRepository;

    @DisplayName("Create/Add Terms")
    @Test
    void Should_ReturnResponseOfNewTermsEntity_When_RequestTerms() {
        Terms entity = Terms.builder()
                .name("Loan Terms")
                .termsDetailUrl("https://randomTermsPage.")
                .build();
        TermsRequestDto request = TermsRequestDto.builder()
                .name("Loan Terms")
                .termsDetailUrl("https://randomTermsPage")
                .build();
        when(termsRepository.save(any(Terms.class))).thenReturn(entity);
        TermsResponseDto actual = termsService.create(request);
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isSameAs(entity.getName());
        assertThat(actual.getTermsDetailUrl()).isSameAs(entity.getTermsDetailUrl());
    }

    @DisplayName("Get a Terms by id")
    @Test
    void Should_ReturnResponseOfExistTermsEntity_When_RequestExistTermsId() {
        Long termsId = 1L;
        Terms entity = Terms.builder()
                .termsId(1L)
                .build();
        when(termsRepository.findById(termsId)).thenReturn(Optional.of(entity));
        TermsResponseDto actual = termsService.get(termsId);
        assertThat(actual.getTermsId()).isSameAs(termsId);
    }

    @DisplayName("Get All Terms")
    @Test
    void Should_ReturnAllResponseOfExistTermsEntities_When_RequestTermsList() {
        Terms terms1 = Terms.builder()
                .name("Terms 1")
                .termsDetailUrl("https://randomTermsPage/terms1")
                .build();
        Terms terms2 = Terms.builder()
                .name("Terms 2")
                .termsDetailUrl("https://randomTermsPage/terms2")
                .build();
        List<Terms> list = new ArrayList<>(Arrays.asList(terms1, terms2));
        when(termsRepository.findAll()).thenReturn(list);
        List<TermsResponseDto> actual = termsService.getAll();
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isSameAs(list.size());
    }

    @DisplayName("Update a Terms")
    @Test
    void Should_ReturnUpdatedResponseOfExistTermsEntity_When_RequestUpdateExistTermsInfo() {
        Long termsId = 1L;
        Terms entity = Terms.builder()
                .termsId(1L)
                .name("Terms 1")
                .termsDetailUrl("https://randomTermsPage/terms1")
                .build();
        TermsRequestDto request = TermsRequestDto.builder()
                .name("Terms 2")
                .termsDetailUrl("https://randomTermsPage/terms2")
                .build();
        when(termsRepository.findById(termsId)).thenReturn(Optional.of(entity));
        TermsResponseDto actual = termsService.update(termsId, request);
        assertThat(actual.getTermsId()).isSameAs(termsId);
        assertThat(actual.getName()).isSameAs(request.getName());
        assertThat(actual.getTermsDetailUrl()).isSameAs(request.getTermsDetailUrl());
    }

    @DisplayName("Delete a Terms")
    @Test
    void Should_ReturnDeletedResponseOfExistTermsEntity_When_RequestExistTermsId() {
        Long termsId = 1L;
        Terms entity = Terms.builder()
                .termsId(1L)
                .name("Terms 1")
                .termsDetailUrl("https://randomTermsPage/terms1")
                .build();
        when(termsRepository.findById(termsId)).thenReturn(Optional.of(entity));
        termsService.delete(termsId);
        assertThat(entity.getIsDeleted()).isSameAs(true);
    }

}