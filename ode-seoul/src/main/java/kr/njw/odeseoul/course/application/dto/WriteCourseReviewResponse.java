package kr.njw.odeseoul.course.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WriteCourseReviewResponse {
    @Schema(description = "리뷰 아이디", example = "1")
    private Long id;
    @Schema(description = "리뷰 작성일시", type = "string", example = "2022-10-17T13:01:53")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
