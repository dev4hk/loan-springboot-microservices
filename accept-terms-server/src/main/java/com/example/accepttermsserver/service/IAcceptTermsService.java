package com.example.accepttermsserver.service;

import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.dto.AcceptTermsResponseDto;

import java.util.List;

public interface IAcceptTermsService {

    List<AcceptTermsResponseDto> create(AcceptTermsRequestDto acceptTermsRequestDto);

}
