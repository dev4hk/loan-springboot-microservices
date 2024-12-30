package com.example.judgement_server.dto;

import com.example.judgement_server.exception.BaseException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Schema(
        name = "Response",
        description = "Schema to hold response information"
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> implements Serializable {

    @Schema(
            description = "Schema to hold result information"
    )
    private ResultObject result;

    @Schema(
            description = "Schema to hold data information"
    )
    private T data;

    public ResponseDTO(ResultObject result) {
        this.result = result;
    }

    public ResponseDTO(T data) {
        this.data = data;
    }

    public static <T> ResponseDTO<T> ok() {
        return new ResponseDTO<>(ResultObject.getSuccess());
    }

    public static <T> ResponseDTO<T> ok(T data) {
        return new ResponseDTO<>(ResultObject.getSuccess(), data);
    }

    public static <T> ResponseDTO<T> response(T data) {
        return new ResponseDTO<>(ResultObject.getSuccess(), data);
    }

    public ResponseDTO(BaseException ex) {
        this.result = new ResultObject(ex);
    }
}