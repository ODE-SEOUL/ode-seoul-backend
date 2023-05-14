package kr.njw.odeseoul.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.njw.odeseoul.user.entity.User;
import lombok.Data;

@Data
public class LoginResponse {
    @Schema(description = "오디서울 아이디 (소셜 아이디 X)", example = "1")
    private Long id;
    private SocialProfile socialProfile = new SocialProfile();
    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    private String accessToken;
    @Schema(example = "gGf9WScvJwqRtJxlbgguF6OQi3xjcBhg")
    private String refreshToken;
    @Schema(description = """
            회원가입 상태

            BEFORE_REGISTERED: 회원가입 필요, 회원가입 API 외 엑세스 토큰을 요구하는 API를 호출할 수 없음 (호출시 code 403 발생)\040\040
            REGISTERED: 회원가입 완료됨""")
    private User.UserSignupStatus signupStatus;

    @Data
    public static class SocialProfile {
        @Schema(description = "현재 시점의 소셜 닉네임 (오디서울 상의 닉네임 X, 회원가입 절차에서만 활용 요망)", example = "njw1204")
        private String nickname;

        @Schema(description = "현재 시점의 소셜 프로필 이미지 (오디서울 상의 프로필 이미지 X, 회원가입 절차에서만 활용 요망)",
                example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg")
        private String profileImage;
    }
}
