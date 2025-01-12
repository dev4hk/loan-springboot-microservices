package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.CounselClient;
import com.example.applicationserver.client.JudgementClient;
import com.example.applicationserver.client.dto.CounselResponseDto;
import com.example.applicationserver.client.dto.JudgementResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JudgementFallback implements JudgementClient {

    private static final Logger logger = LoggerFactory.getLogger(JudgementFallback.class);

    @Override
    public ResponseDTO<JudgementResponseDto> getJudgmentOfApplication(Long applicationId) {
        logger.error("JudgementFallback - getJudgmentOfApplication invoked");
        throw new BaseException(
                ResultType.SERVICE_UNAVAILABLE,
                "Judgement service unavailable",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
