package kr.njw.odeseoul.user.application;

import kr.njw.odeseoul.user.application.dto.EditPickedCoursesRequest;
import kr.njw.odeseoul.user.application.dto.EditProfileRequest;

public interface UserService {
    void editProfile(EditProfileRequest request);

    void editPickedCourses(EditPickedCoursesRequest request);
}
