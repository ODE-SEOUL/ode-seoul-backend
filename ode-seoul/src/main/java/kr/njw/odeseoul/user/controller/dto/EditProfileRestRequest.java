package kr.njw.odeseoul.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EditProfileRestRequest {
    @Schema(description = "닉네임", example = "닉넴", nullable = true)
    private String nickname;
    @Schema(description = "프로필 이미지", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg", nullable = true)
    private String profileImage;
    @Schema(description = "사는 곳 (서울시 자치구 행정동코드)", example = "1135000000", nullable = true)
    private String locationCode;
}
