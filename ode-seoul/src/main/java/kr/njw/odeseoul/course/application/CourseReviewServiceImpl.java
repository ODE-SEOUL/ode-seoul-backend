package kr.njw.odeseoul.course.application;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.course.application.dto.WriteCourseReviewRequest;
import kr.njw.odeseoul.course.application.dto.WriteCourseReviewResponse;
import kr.njw.odeseoul.course.entity.Course;
import kr.njw.odeseoul.course.entity.CourseReview;
import kr.njw.odeseoul.course.repository.CourseRepository;
import kr.njw.odeseoul.course.repository.CourseReviewRepository;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CourseReviewServiceImpl implements CourseReviewService {
    private final CourseReviewRepository courseReviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public WriteCourseReviewResponse writeCourseReview(WriteCourseReviewRequest request) {
        Course course = this.courseRepository.findByIdAndDeletedAtIsNull(request.getCourseId()).orElse(null);
        User user = this.userRepository.findByIdAndDeletedAtIsNull(request.getUserId()).orElse(null);

        if (course == null) {
            throw new BaseException(BaseResponseStatus.WRITE_COURSE_REVIEW_ERROR_NOT_FOUND_COURSE);
        }

        if (user == null) {
            throw new BaseException(BaseResponseStatus.WRITE_COURSE_REVIEW_ERROR_NOT_FOUND_USER);
        }

        if (this.courseReviewRepository.findFirstByCourseIdAndUserIdAndDeletedAtIsNull(request.getCourseId(), request.getUserId()).isPresent()) {
            throw new BaseException(BaseResponseStatus.WRITE_COURSE_REVIEW_ERROR_ALREADY_WRITTEN);
        }

        CourseReview courseReview = CourseReview.builder()
                .course(course)
                .user(user)
                .score(request.getScore())
                .content(StringUtils.trimToEmpty(request.getContent()))
                .image(StringUtils.trimToEmpty(request.getImage()))
                .build();

        this.courseReviewRepository.saveAndFlush(courseReview);

        WriteCourseReviewResponse response = new WriteCourseReviewResponse();
        response.setId(courseReview.getId());
        response.setScore(courseReview.getScore());
        response.setContent(courseReview.getContent());
        response.setImage(courseReview.getImage());
        response.setCreatedAt(courseReview.getCreatedAt());
        return response;
    }
}
