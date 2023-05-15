package kr.njw.odeseoul.course.application;

import kr.njw.odeseoul.course.application.dto.WriteCourseReviewRequest;
import kr.njw.odeseoul.course.application.dto.WriteCourseReviewResponse;

public interface CourseReviewService {
    WriteCourseReviewResponse writeCourseReview(WriteCourseReviewRequest request);
}
