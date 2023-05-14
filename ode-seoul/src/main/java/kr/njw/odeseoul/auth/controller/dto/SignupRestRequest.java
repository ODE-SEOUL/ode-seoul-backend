package kr.njw.odeseoul.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRestRequest {
    @Schema(description = "오디서울 닉네임", example = "닉넴")
    @NotBlank(message = "must not be blank")
    private String nickname;
    @Schema(description = "사는 곳 (서울시 자치구 행정동코드)", example = "1135000000", nullable = true)
    private String locationCode;
}
