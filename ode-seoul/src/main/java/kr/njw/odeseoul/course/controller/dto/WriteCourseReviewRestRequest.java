package kr.njw.odeseoul.course.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WriteCourseReviewRestRequest {
    @Schema(description = "코스 아이디", example = "4")
    @NotNull(message = "must not be null")
    private Long courseId;
    @Schema(description = "별점", example = "5")
    @NotNull(message = "must not be null")
    @Min(value = 0, message = "must be greater than or equal to 0")
    @Max(value = 5, message = "must be less than or equal to 5")
    private int score;
    @Schema(example = "걷기에 너무 좋아요! 추천합니다")
    private String content;
    @Schema(description = "리뷰 첨부 이미지 URL", example = "https://ik.imagekit.io/njw1204/tr:w-720,h-720,c-at_max/ode-seoul/20230515175443_pjLt7oeIx")
    private String image;
}
