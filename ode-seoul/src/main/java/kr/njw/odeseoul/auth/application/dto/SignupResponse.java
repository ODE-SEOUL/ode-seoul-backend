package kr.njw.odeseoul.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignupResponse {
    @Schema(description = "오디서울 아이디 (소셜 아이디 X)", example = "1")
    private Long id;
    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    private String accessToken;
    @Schema(example = "gGf9WScvJwqRtJxlbgguF6OQi3xjcBhg")
    private String refreshToken;
}
