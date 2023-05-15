package kr.njw.odeseoul.course.application.dto;

import lombok.Data;

@Data
public class WriteCourseReviewRequest {
    private Long courseId;
    private Long userId;
    private int score;
    private String content;
    private String image;
}
