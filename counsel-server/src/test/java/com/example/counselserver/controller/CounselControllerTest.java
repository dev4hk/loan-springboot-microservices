package com.example.counselserver.controller;

import com.example.counselserver.dto.CounselRequestDto;
import com.example.counselserver.dto.CounselResponseDto;
import com.example.counselserver.dto.ResponseDTO;
import com.example.counselserver.service.ICounselService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CounselControllerTest {

    @InjectMocks
    private CounselController counselController;

    @Mock
    private ICounselService counselService;

    private CounselRequestDto request;
    private CounselResponseDto response;
    private Long counselId = 1L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new CounselRequestDto();
        response = new CounselResponseDto();
    }

    @Test
    public void createCounsel() {
        when(counselService.create(any(CounselRequestDto.class))).thenReturn(response);

        ResponseDTO<CounselResponseDto> result = counselController.create(request);

        assertEquals(response, result.getData());
        verify(counselService, times(1)).create(any(CounselRequestDto.class));
    }

    @Test
    public void getCounsel() {
        when(counselService.get(counselId)).thenReturn(response);

        ResponseDTO<CounselResponseDto> result = counselController.get(counselId);

        assertEquals(response, result.getData());
        verify(counselService, times(1)).get(counselId);
    }

    @Test
    public void getAllCounsel() {
        Pageable pageable = PageRequest.of(0, 5);
        CounselResponseDto counsel1 = CounselResponseDto.builder()
                .counselId(1L)
                .firstname("John")
                .lastname("Doe")
                .build();
        CounselResponseDto counsel2 = CounselResponseDto.builder()
                .counselId(2L)
                .firstname("Jane")
                .lastname("Smith")
                .build();

        List<CounselResponseDto> counselList = List.of(counsel1, counsel2);
        Page<CounselResponseDto> counselPage = new PageImpl<>(counselList, pageable, counselList.size());

        when(counselService.getAll(pageable)).thenReturn(counselPage);

        ResponseDTO<Page<CounselResponseDto>> responseDTO = counselController.getAll(pageable);

        assertNotNull(responseDTO);
        assertEquals(2, responseDTO.getData().getContent().size());  // Check if the list size is correct
        assertEquals(1L, responseDTO.getData().getContent().get(0).getCounselId());
        assertEquals("John", responseDTO.getData().getContent().get(0).getFirstname());
        assertEquals(2L, responseDTO.getData().getContent().get(1).getCounselId());
        assertEquals("Jane", responseDTO.getData().getContent().get(1).getFirstname());

        verify(counselService, times(1)).getAll(pageable);
    }

    @Test
    public void updateCounsel() {
        when(counselService.update(any(Long.class), any(CounselRequestDto.class))).thenReturn(response);

        ResponseDTO<CounselResponseDto> result = counselController.update(counselId, request);

        assertEquals(response, result.getData());
        verify(counselService, times(1)).update(any(Long.class), any(CounselRequestDto.class));
    }

    @Test
    public void deleteCounsel() {
        doNothing().when(counselService).delete(counselId);

        ResponseDTO<CounselResponseDto> result = counselController.delete(counselId);

        verify(counselService, times(1)).delete(counselId);
    }
}
