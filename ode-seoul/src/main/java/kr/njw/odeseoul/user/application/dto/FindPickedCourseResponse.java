package kr.njw.odeseoul.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FindPickedCourseResponse {
    @Schema(example = "1")
    private Long courseId;
    @Schema(example = "강서문화 산책길")
    private String courseName;
    @Schema(description = "대표 이미지", example = "https://gil.seoul.go.kr/view/point/2013/09/26/8530222389919.jpg", nullable = true)
    private String image;
}
