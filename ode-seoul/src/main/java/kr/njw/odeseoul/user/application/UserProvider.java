package kr.njw.odeseoul.user.application;

import kr.njw.odeseoul.user.application.dto.FindPickedCourseResponse;
import kr.njw.odeseoul.user.application.dto.FindUserResponse;

import java.util.List;

public interface UserProvider {
    FindUserResponse findUser(Long id);

    List<FindPickedCourseResponse> findPickedCourses(Long userId);
}
