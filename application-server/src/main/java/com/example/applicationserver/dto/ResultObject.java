package com.example.applicationserver.dto;

import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.exception.BaseException;
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