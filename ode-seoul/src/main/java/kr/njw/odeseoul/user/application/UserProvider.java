package kr.njw.odeseoul.user.application;

import kr.njw.odeseoul.user.application.dto.FindUserResponse;

public interface UserProvider {
    FindUserResponse findUser(Long id);
}
