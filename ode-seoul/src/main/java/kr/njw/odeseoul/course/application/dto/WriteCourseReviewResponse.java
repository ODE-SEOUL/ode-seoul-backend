package kr.njw.odeseoul.course.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WriteCourseReviewResponse {
    @Schema(description = "리뷰 아이디", example = "1")
    private Long id;

    @Schema(description = "별점", example = "5")
    private int score;

    @Schema(example = "걷기에 너무 좋아요! 추천합니다")
    private String content;

    @Schema(description = "리뷰 첨부 이미지 URL", example = "https://ik.imagekit.io/njw1204/tr:w-720,h-720,c-at_max/ode-seoul/20230515175443_pjLt7oeIx")
    private String image;

    @Schema(description = "리뷰 작성일시", type = "string", example = "2022-10-17T13:01:53")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
