package com.example.balance_server.dto;

import com.example.balance_server.constants.ResultType;
import com.example.balance_server.exception.BaseException;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultObject {

    public String code;

    public String desc;

    public ResultObject(ResultType resultType) {
        this.code = resultType.getCode();
        this.desc = resultType.getDesc();
    }

    public ResultObject(ResultType resultCode, String message) {
        this.code = resultCode.getCode();
        this.desc = message;
    }

    public ResultObject(BaseException e) {
        this.code = e.getCode();
        this.desc = e.getDesc();
    }

    public static ResultObject getSuccess() {
        return new ResultObject(ResultType.SUCCESS);
    }
}