package com.example.applicationserver.exception;

import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.dto.ResultObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CustomErrorDecoder implements ErrorDecoder {
    private static final Logger logger = LoggerFactory.getLogger(CustomErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMessage;
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());

        logger.error("Error in method {} with status {}: {}", methodKey, response.status(), response.reason());

        try (InputStream bodyIs = response.body().asInputStream()) {
            Map<String, Object> responseMap = objectMapper.readValue(bodyIs, Map.class);
            logger.error("Error response map: {}", responseMap);

            ResultObject resultObject = objectMapper.convertValue(responseMap.get("result"), ResultObject.class);
            Object data = responseMap.get("data");

            errorMessage = extractErrorMessage(data);

            ResponseDTO<Object> errorResponse = new ResponseDTO<>(resultObject, data);

            if (response.status() == 400) {
                return new FeignValidationException(errorResponse);
            }

            ResultType resultType = determineResultType(response.status());
            return new BaseException(resultType, errorMessage, httpStatus);
        } catch (IOException e) {
            logger.error("Error reading response body", e);
            return defaultErrorDecoder.decode(methodKey, response);
        } catch (Exception e) {
            logger.error("General exception occurred", e);
            return new BaseException(ResultType.SYSTEM_ERROR, "General error occurred", httpStatus);
        }
    }

    private String extractErrorMessage(Object data) {
        if (data instanceof Map) {
            Map<?, ?> dataMap = (Map<?, ?>) data;
            if (dataMap.containsKey("errorMessage")) {
                return dataMap.get("errorMessage").toString();
            }
        }
        return null;
    }

    private ResultType determineResultType(int statusCode) {
        switch (statusCode) {
            case 400:
                return ResultType.BAD_REQUEST;
            case 404:
                return ResultType.RESOURCE_NOT_FOUND;
            case 500:
                return ResultType.SYSTEM_ERROR;
            default:
                return ResultType.SYSTEM_ERROR;
        }
    }
}

