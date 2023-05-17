package kr.njw.odeseoul.user.application;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.course.entity.Course;
import kr.njw.odeseoul.course.repository.CourseRepository;
import kr.njw.odeseoul.location.entity.Location;
import kr.njw.odeseoul.location.repository.LocationRepository;
import kr.njw.odeseoul.user.application.dto.EditPickedCoursesRequest;
import kr.njw.odeseoul.user.application.dto.EditProfileRequest;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.entity.UserPickedCourse;
import kr.njw.odeseoul.user.repository.UserPickedCourseRepository;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserPickedCourseRepository userPickedCourseRepository;
    private final CourseRepository courseRepository;
    private final LocationRepository locationRepository;

    public void editProfile(EditProfileRequest request) {
        boolean locationWillChange = (request.getLocationCode() != null);
        boolean locationWillAssign = StringUtils.isNotBlank(request.getLocationCode());

        User user = this.userRepository.findByIdAndDeletedAtIsNull(request.getId()).orElse(null);
        Location location = this.locationRepository.findByCodeAndDeletedAtIsNull(request.getLocationCode()).orElse(null);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.EDIT_PROFILE_ERROR_BAD_USER);
        }

        if (locationWillAssign && location == null) {
            throw new BaseException(BaseResponseStatus.EDIT_PROFILE_ERROR_BAD_LOCATION);
        }

        if (location != null && !location.isSeoulGugun()) {
            throw new BaseException(BaseResponseStatus.EDIT_PROFILE_ERROR_BAD_LOCATION);
        }

        user.editBasicProfile(request.getNickname(), request.getProfileImage());

        if (locationWillChange) {
            user.setLocation(location);
        }
    }

    public void editPickedCourses(EditPickedCoursesRequest request) {
        User user = this.userRepository.findByIdAndDeletedAtIsNull(request.getUserId()).orElse(null);
        Course course = this.courseRepository.findByIdAndDeletedAtIsNull(request.getCourseId()).orElse(null);
        UserPickedCourse userPickedCourse = this.userPickedCourseRepository.findByUserIdAndCourseIdAndDeletedAtIsNull(
                request.getUserId(), request.getCourseId()).orElse(null);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.EDIT_PICKED_COURSES_ERROR_BAD_USER);
        }

        if (course == null) {
            throw new BaseException(BaseResponseStatus.EDIT_PICKED_COURSES_ERROR_BAD_COURSE);
        }

        switch (request.getEditType()) {
            case ADD -> {
                if (userPickedCourse == null) {
                    userPickedCourse = UserPickedCourse.builder()
                            .user(user)
                            .course(course)
                            .build();
                    this.userPickedCourseRepository.saveAndFlush(userPickedCourse);
                }
            }
            case REMOVE -> {
                if (userPickedCourse != null) {
                    userPickedCourse.delete();
                }
            }
        }
    }
}
