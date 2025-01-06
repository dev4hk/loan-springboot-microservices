package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.JudgementClient;
import com.example.applicationserver.client.dto.JudgementResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.dto.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JudgementFallback implements JudgementClient {

    private static final Logger logger = LoggerFactory.getLogger(JudgementFallback.class);

    @Override
    public ResponseDTO<JudgementResponseDto> getJudgmentOfApplication(Long applicationId) {
        logger.error("JudgementFallback getJudgmentOfApplication() invoked for applicationId: {}", applicationId);
        JudgementResponseDto fallbackResponse = JudgementResponseDto.builder()
                .applicationId(applicationId)
                .build();

        ResultObject resultObject = ResultObject.builder()
                .code(ResultType.SYSTEM_ERROR.getCode())
                .desc("Error fetching judgement")
                .build();

        return new ResponseDTO<>(resultObject, fallbackResponse);
    }
}
