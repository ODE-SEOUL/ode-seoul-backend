package kr.njw.odeseoul.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class BaseResponse<T> {
    @Schema(example = "200")
    private final int code;
    @Schema(example = "요청에 성공하였습니다.")
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public BaseResponse(T result) {
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.result = result;
    }

    public BaseResponse(BaseResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public BaseResponse(T result, BaseResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.result = result;
    }
}
