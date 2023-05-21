package kr.njw.odeseoul.user.application;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.user.application.dto.FindPickedCourseResponse;
import kr.njw.odeseoul.user.application.dto.FindUserResponse;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.entity.UserPickedCourse;
import kr.njw.odeseoul.user.repository.UserPickedCourseRepository;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserProviderImpl implements UserProvider {
    private final UserRepository userRepository;
    private final UserPickedCourseRepository userPickedCourseRepository;

    @Override
    public FindUserResponse findUser(Long id) {
        User user = this.userRepository.findByIdAndDeletedAtIsNull(id).orElse(null);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.FIND_USER_ERROR_NOT_FOUND_USER);
        }

        FindUserResponse response = new FindUserResponse();
        response.setId(user.getId());
        response.setNickname(user.getNickname());
        response.setProfileImage(user.getProfileImage());
        response.setLocationCode(user.getLocationCode());
        response.setSignupStatus(user.getSignupStatus());
        return response;
    }

    public List<FindPickedCourseResponse> findPickedCourses(Long userId) {
        User user = this.userRepository.findByIdAndDeletedAtIsNull(userId).orElse(null);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.FIND_PICKED_COURSES_ERROR_BAD_USER);
        }

        List<UserPickedCourse> userPickedCourses = this.userPickedCourseRepository.findAllByUserIdAndDeletedAtIsNullOrderByIdDesc(userId);

        return userPickedCourses.stream().map(userPickedCourse -> {
            FindPickedCourseResponse response = new FindPickedCourseResponse();
            response.setCourseId(userPickedCourse.getCourse().getId());
            response.setCourseName(userPickedCourse.getCourse().getName());
            response.setImage(userPickedCourse.getCourse().getImage());
            return response;
        }).collect(Collectors.toList());
    }
}
