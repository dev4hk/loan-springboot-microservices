package com.example.applicationserver.service.impl;

import com.example.applicationserver.client.*;
import com.example.applicationserver.client.dto.*;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ApplicationRequestDto;
import com.example.applicationserver.dto.ApplicationResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.dto.ResultObject;
import com.example.applicationserver.entity.Application;
import com.example.applicationserver.exception.BaseException;
import com.example.applicationserver.exception.CustomFeignException;
import com.example.applicationserver.repository.ApplicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @InjectMocks
    ApplicationServiceImpl applicationService;

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    TermsClient termsClient;

    @Mock
    AcceptTermsClient acceptTermsClient;

    @Mock
    JudgementClient judgementClient;

    @Mock
    CounselClient counselClient;

    @Mock
    FileStorageClient fileStorageClient;

    @Mock
    StreamBridge streamBridge;

    @DisplayName("Create Application")
    @Test
    void Should_ReturnResponseOfNewApplicationEntity_When_RequestCreateApplication() {
        String email = "mail@abcd.efg";
        Application entity = Application.builder()
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("1234567890")
                .email(email)
                .hopeAmount(BigDecimal.valueOf(50000))
                .build();

        ApplicationRequestDto request = ApplicationRequestDto.builder()
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("1234567890")
                .email(email)
                .hopeAmount(BigDecimal.valueOf(50000))
                .build();

        when(applicationRepository.findByEmail(eq(email))).thenReturn(Optional.empty());
        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(entity);
        ApplicationResponseDto actual = applicationService.create(request);
        assertThat(actual).isNotNull();
        assertThat(actual.getHopeAmount()).isSameAs(entity.getHopeAmount());
        assertThat(actual.getFirstname()).isSameAs(entity.getFirstname());
    }

    @DisplayName("Create Application throw exception with application already exists")
    @Test
    void Should_Throws_Exception_When_RequestCreateApplication_With_Existing_Email() {
        String email = "mail@abcd.efg";
        Application entity = Application.builder()
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("1234567890")
                .email(email)
                .hopeAmount(BigDecimal.valueOf(50000))
                .build();

        ApplicationRequestDto request = ApplicationRequestDto.builder()
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("1234567890")
                .email(email)
                .hopeAmount(BigDecimal.valueOf(50000))
                .build();

        when(applicationRepository.findByEmail(eq(email))).thenReturn(Optional.of(entity));
        assertThrows(BaseException.class, () -> applicationService.create(request));
    }

    @DisplayName("Get An Application by ApplicationId")
    @Test
    void Should_ReturnResponseOfExistApplicationEntity_When_RequestExistApplicationId() {
        Long applicationId = 1L;
        Application entity = Application.builder()
                .applicationId(1L)
                .build();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(entity));
        when(counselClient.getByEmail(entity.getEmail())).thenReturn(
                new ResponseDTO<>(
                        CounselResponseDto.builder()
                                .firstname("firstname")
                                .lastname("lastname")
                                .cellPhone("0123456789")
                                .email("email@email.com")
                                .address1("address")
                                .appliedAt(LocalDateTime.now())
                                .counselId(1L)
                                .memo("memo")
                                .build()
                )
        );
        when(fileStorageClient.getFilesInfo(anyLong())).thenReturn(
                new ResponseDTO<>(
                        List.of(FileResponseDto.builder().build())
                )
        );
        ApplicationResponseDto actual = applicationService.get(applicationId);
        assertThat(actual).isNotNull();
        assertThat(actual.getApplicationId()).isSameAs(applicationId);
    }

    @DisplayName("Get An Application by email")
    @Test
    void Should_ReturnResponseOfExistApplicationEntity_When_RequestExistEmail() {
        String email = "email@email.com";
        Application entity = Application.builder()
                .applicationId(1L)
                .email(email)
                .build();
        when(applicationRepository.findByEmail(email)).thenReturn(Optional.ofNullable(entity));
        when(counselClient.getByEmail(entity.getEmail())).thenReturn(
                new ResponseDTO<>(
                        CounselResponseDto.builder()
                                .firstname("firstname")
                                .lastname("lastname")
                                .cellPhone("0123456789")
                                .email("email@email.com")
                                .address1("address")
                                .appliedAt(LocalDateTime.now())
                                .counselId(1L)
                                .memo("memo")
                                .build()
                )
        );
        when(fileStorageClient.getFilesInfo(anyLong())).thenReturn(
                new ResponseDTO<>(
                        List.of(FileResponseDto.builder().build())
                )
        );
        ApplicationResponseDto actual = applicationService.getByEmail(email);
        assertThat(actual).isNotNull();
        assertThat(actual.getEmail()).isSameAs(email);
    }

    @DisplayName("Get non-existing Application by id")
    @Test
    void Should_ThrowException_When_RequestNonExistApplicationId() {
        Long applicationId = 1L;
        when(applicationRepository.findById(applicationId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertThrows(BaseException.class, () -> applicationService.get(applicationId));
    }

    @DisplayName("Get non-existing Application by email")
    @Test
    void Should_ThrowException_When_RequestNonExistEmail() {
        String email = "email@email.com";
        when(applicationRepository.findByEmail(email)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertThrows(BaseException.class, () -> applicationService.getByEmail(email));
    }

    @DisplayName("Should return paginated application response when requested")
    @Test
    void should_Return_Paginated_Application_Response() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("appliedAt").descending());
        List<Application> applications = List.of(
                Application.builder().applicationId(1L).firstname("John").lastname("Doe").email("john@example.com").build(),
                Application.builder().applicationId(2L).firstname("Jane").lastname("Doe").email("jane@example.com").build()
        );
        Page<Application> applicationPage = new PageImpl<>(applications, pageable, applications.size());

        when(applicationRepository.findAll(pageable)).thenReturn(applicationPage);

        Page<ApplicationResponseDto> result = applicationService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getApplicationId()).isEqualTo(1L);
        assertThat(result.getContent().get(1).getApplicationId()).isEqualTo(2L);

        verify(applicationRepository, times(1)).findAll(pageable);
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
        when(streamBridge.send(anyString(), any())).thenReturn(true);
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

    @DisplayName("accept terms")
    @Test
    void should_accept_terms() {
        Long applicationId = 1L;
        Application entity = Application.builder()
                .applicationId(1L)
                .build();
        AcceptTermsRequestDto request = AcceptTermsRequestDto.builder()
                .applicationId(applicationId)
                .termsIds(List.of(1L, 2L))
                .build();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(entity));
        when(termsClient.getAll()).thenReturn(
                new ResponseDTO<>(
                        ResultObject.builder()
                                .code(ResultType.SUCCESS.getCode())
                                .desc(ResultType.SUCCESS.getDesc())
                                .build(),
                        List.of(
                                TermsResponseDto
                                        .builder()
                                        .name("terms1")
                                        .termsId(1L)
                                        .build(),
                                TermsResponseDto
                                        .builder()
                                        .name("terms2")
                                        .termsId(2L)
                                        .build()
                        )
                ));
        when(acceptTermsClient.create(request)).thenReturn(new ResponseDTO<>(
                ResultObject.builder()
                        .code(ResultType.SUCCESS.getCode())
                        .desc(ResultType.SUCCESS.getDesc())
                        .build(),
                List.of(
                        AcceptTermsResponseDto
                                .builder()
                                .applicationId(applicationId)
                                .termsId(1L)
                                .build(),
                        AcceptTermsResponseDto
                                .builder()
                                .applicationId(applicationId)
                                .termsId(2L)
                                .build()
                )));

        applicationService.acceptTerms(applicationId, request);

        verify(applicationRepository, times(1)).findById(applicationId);
        verify(termsClient, times(1)).getAll();
        verify(acceptTermsClient, times(1)).create(request);

    }

    @DisplayName("accept terms throw exception with non-existing applicationId")
    @Test
    void Should_ThrowException_When_RequestAcceptTermsWithNonExistingApplicationId() {
        Long applicationId = 1L;
        AcceptTermsRequestDto request = AcceptTermsRequestDto.builder()
                .applicationId(applicationId)
                .termsIds(List.of(1L, 2L))
                .build();
        when(applicationRepository.findById(applicationId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertThrows(BaseException.class, () -> applicationService.acceptTerms(applicationId, request));
    }

    @DisplayName("accept terms throw exception with empty termsIds")
    @Test
    void Should_ThrowException_When_RequestAcceptTermsWithEmptyTermsIds() {
        Long applicationId = 1L;
        AcceptTermsRequestDto request = AcceptTermsRequestDto.builder()
                .applicationId(applicationId)
                .termsIds(List.of())
                .build();
        assertThrows(BaseException.class, () -> applicationService.acceptTerms(applicationId, request));
    }

    @DisplayName("accept terms throw exception with different size of terms list from terms service and request terms list")
    @Test
    void Should_ThrowException_When_RequestAcceptTermsWithDifferentSizeOfTerms() {
        Long applicationId = 1L;
        AcceptTermsRequestDto request = AcceptTermsRequestDto.builder()
                .applicationId(applicationId)
                .termsIds(List.of(1L))
                .build();
        lenient().when(termsClient.getAll()).thenReturn(
                new ResponseDTO<>(
                        List.of(
                                TermsResponseDto.builder()
                                        .name("terms1")
                                        .termsId(1L)
                                        .build(),
                                TermsResponseDto.builder()
                                        .name("terms2")
                                        .termsId(2L)
                                        .build()
                        ))
        );
        assertThrows(BaseException.class, () -> applicationService.acceptTerms(applicationId, request));
    }

    @DisplayName("accept terms throw exception with different terms Ids")
    @Test
    void Should_ThrowException_When_RequestAcceptTermsWithDifferentTermsIds() {
        Long applicationId = 1L;
        AcceptTermsRequestDto request = AcceptTermsRequestDto.builder()
                .applicationId(applicationId)
                .termsIds(List.of(1L, 3L))
                .build();
        lenient().when(termsClient.getAll()).thenReturn(
                new ResponseDTO<>(
                        List.of(
                                TermsResponseDto
                                        .builder()
                                        .name("terms1")
                                        .termsId(1L)
                                        .build(),
                                TermsResponseDto
                                        .builder()
                                        .name("terms2")
                                        .termsId(2L)
                                        .build()
                        )
                ));
        assertThrows(BaseException.class, () -> applicationService.acceptTerms(applicationId, request));
    }

    @DisplayName("contract")
    @Test
    void should_contract() {
        Long applicationId = 1L;
        Application entity = Application.builder()
                .applicationId(1L)
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("1234567890")
                .email("mail@abcd.efg")
                .hopeAmount(BigDecimal.valueOf(50000))
                .approvalAmount(BigDecimal.valueOf(50000))
                .build();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(entity));
        when(judgementClient.getJudgmentOfApplication(applicationId)).thenReturn(
                new ResponseDTO<>(
                        ResultObject.builder()
                                .code(ResultType.SUCCESS.getCode())
                                .desc(ResultType.SUCCESS.getDesc())
                                .build(),
                        JudgementResponseDto.builder()
                                .judgementId(1L)
                                .applicationId(1L)
                                .firstname("firstname")
                                .lastname("lastname")
                                .approvalAmount(BigDecimal.valueOf(1000.00))
                                .build()
                )
        );
        when(streamBridge.send(anyString(), any())).thenReturn(true);
        ApplicationResponseDto applicationResponseDto = applicationService.contract(applicationId);
        verify(applicationRepository, times(1)).findById(applicationId);
        verify(judgementClient, times(1)).getJudgmentOfApplication(applicationId);
        assertNotNull(applicationResponseDto.getContractedAt());
    }

    @DisplayName("contract throw exception with application not found")
    @Test
    void should_throw_exception_when_contract_for_non_existing_application() {
        Long applicationId = 1L;
        when(applicationRepository.findById(applicationId)).thenThrow(new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        assertThrows(BaseException.class, () -> applicationService.contract(applicationId));
    }

    @DisplayName("contract throw exception when judgement client throws exception")
    @Test
    void should_throw_exception_when_contract_with_null_judgement_data() {
        Long applicationId = 1L;
        Application entity = Application.builder()
                .applicationId(1L)
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("1234567890")
                .email("mail@abcd.efg")
                .hopeAmount(BigDecimal.valueOf(50000))
                .approvalAmount(BigDecimal.valueOf(50000))
                .build();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(entity));
        doThrow(CustomFeignException.class).when(judgementClient).getJudgmentOfApplication(applicationId);

        assertThrows(CustomFeignException.class, () -> applicationService.contract(applicationId));
    }

    @DisplayName("contract throw exception with null approval amount")
    @Test
    void should_throw_exception_when_contract_for_application_with_null_approval_amount() {
        Long applicationId = 1L;
        Application entity = Application.builder()
                .applicationId(1L)
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("1234567890")
                .email("mail@abcd.efg")
                .hopeAmount(BigDecimal.valueOf(50000))
                .build();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(entity));
        when(judgementClient.getJudgmentOfApplication(applicationId)).thenReturn(
                new ResponseDTO<>(
                        ResultObject.builder()
                                .code(ResultType.SUCCESS.getCode())
                                .desc(ResultType.SUCCESS.getDesc())
                                .build(),
                        JudgementResponseDto.builder()
                                .judgementId(1L)
                                .applicationId(1L)
                                .firstname("firstname")
                                .lastname("lastname")
                                .approvalAmount(BigDecimal.valueOf(1000.00))
                                .build()
                )
        );
        assertThrows(BaseException.class, () -> applicationService.contract(applicationId));
    }
}