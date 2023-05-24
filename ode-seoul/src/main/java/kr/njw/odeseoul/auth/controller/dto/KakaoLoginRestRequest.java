package kr.njw.odeseoul.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class KakaoLoginRestRequest {
    @Schema(description = "카카오 인증 요청 후 프론트에서 리다이렉션을 통해 받은 인가 코드", example = "YAwAcul7-ZJArXI3NuVO7vnW7jYg_4wkqwtPM2-vnKnxRHoTWD34rAUoZ2yTZh5sV0ygngoqJVMAAAGIGZNGug")
    @NotEmpty(message = "must not be empty")
    private String authorizationCode;

    @Schema(description = "카카오 인증 요청 당시 프론트에서 사용한 Redirect URI 값 (카카오 개발자센터에 등록한 값과 정확히 일치해야 됨)", example = "http://localhost:8080/oauth")
    @NotEmpty(message = "must not be empty")
    private String redirectedUri;
}
