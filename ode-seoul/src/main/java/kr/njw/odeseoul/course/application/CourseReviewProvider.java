package kr.njw.odeseoul.course.application;

import kr.njw.odeseoul.course.application.dto.FindCourseReviewResponse;
import kr.njw.odeseoul.course.application.dto.FindCourseReviewsRequest;

import java.util.List;

public interface CourseReviewProvider {
    List<FindCourseReviewResponse> findCourseReviews(FindCourseReviewsRequest request);
}
