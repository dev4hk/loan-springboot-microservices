package com.example.repayment_server.exception;

import com.example.repayment_server.constants.ResultType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class BaseException extends RuntimeException {

    private String code = "";
    private String desc = "";
    private HttpStatus httpStatus;
    private String extraMessage = "";

    public BaseException(ResultType resultType, HttpStatus httpStatus) {
        super(resultType.getDesc());
        this.code = resultType.getCode();
        this.desc = resultType.getDesc();
        this.httpStatus = httpStatus;
    }

    public BaseException(ResultType resultType, String extraMessage, HttpStatus httpStatus) {
        super(resultType.getDesc() + " - " + extraMessage);
        this.code = resultType.getCode();
        this.desc = resultType.getDesc();
        this.extraMessage = extraMessage;
        this.httpStatus = httpStatus;
    }
}

