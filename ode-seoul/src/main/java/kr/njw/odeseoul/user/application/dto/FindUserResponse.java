package kr.njw.odeseoul.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.njw.odeseoul.user.entity.User;
import lombok.Data;

@Data
public class FindUserResponse {
    @Schema(description = "오디서울 아이디", example = "1")
    private Long id;
    @Schema(description = "오디서울 닉네임", example = "닉넴")
    private String nickname;
    @Schema(description = "오디서울 프로필 이미지", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", nullable = true)
    private String profileImage;
    @Schema(description = "사는 곳 (서울시 자치구 행정동코드)", example = "1135000000", nullable = true)
    private String locationCode;
    @Schema(description = "회원가입 상태")
    private User.UserSignupStatus signupStatus;
}
