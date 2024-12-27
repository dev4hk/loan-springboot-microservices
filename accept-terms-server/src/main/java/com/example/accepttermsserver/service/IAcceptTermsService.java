package com.example.accepttermsserver.service;

import com.example.accepttermsserver.dto.AcceptTermsRequestDto;
import com.example.accepttermsserver.dto.AcceptTermsResponseDto;

public interface IAcceptTermsService {

    AcceptTermsResponseDto create(AcceptTermsRequestDto acceptTermsRequestDto);
    AcceptTermsResponseDto get(Long acceptTermsId);
    AcceptTermsResponseDto update(Long acceptTermsId, AcceptTermsRequestDto acceptTermsRequestDto);
    void delete(Long acceptTermsId);

}
