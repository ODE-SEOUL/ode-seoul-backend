package kr.njw.odeseoul.course.application;

import kr.njw.odeseoul.course.application.dto.FindCourseReviewResponse;
import kr.njw.odeseoul.course.application.dto.FindCourseReviewsRequest;
import kr.njw.odeseoul.course.entity.CourseReview;
import kr.njw.odeseoul.course.repository.CourseReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CourseReviewProviderImpl implements CourseReviewProvider {
    private final CourseReviewRepository courseReviewRepository;

    public List<FindCourseReviewResponse> findCourseReviews(FindCourseReviewsRequest request) {
        List<CourseReview> reviews = switch (request.getFindType()) {
            case BY_COURSE_ID ->
                    this.courseReviewRepository.findAllByCourseIdAndDeletedAtIsNullOrderByIdDesc(request.getCourseId());
            case BY_USER_ID ->
                    this.courseReviewRepository.findAllByUserIdAndDeletedAtIsNullOrderByIdDesc(request.getUserId());
        };

        return reviews.stream().map(courseReview -> {
            FindCourseReviewResponse response = new FindCourseReviewResponse();
            response.setId(courseReview.getId());
            response.setCourseId(courseReview.getCourse().getId());
            response.setUserId(courseReview.getUser().getId());
            response.setScore(courseReview.getScore());
            response.setContent(courseReview.getContent());
            response.setImage(courseReview.getImage());
            response.setCreatedAt(courseReview.getCreatedAt());
            return response;
        }).collect(Collectors.toList());
    }
}
