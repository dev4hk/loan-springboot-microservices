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

}