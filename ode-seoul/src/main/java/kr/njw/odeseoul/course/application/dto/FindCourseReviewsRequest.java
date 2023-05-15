package kr.njw.odeseoul.course.application.dto;

import lombok.Data;

@Data
public class FindCourseReviewsRequest {
    private FindType findType;
    private Long courseId;
    private Long userId;

    public enum FindType {
        BY_COURSE_ID,
        BY_USER_ID
    }
}
