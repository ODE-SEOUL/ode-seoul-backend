package kr.njw.odeseoul.auth.application;

import kr.njw.odeseoul.auth.application.dto.AbstractLoginRequest;
import kr.njw.odeseoul.auth.application.dto.LoginResponse;

public interface LoginService<T extends AbstractLoginRequest> {
    LoginResponse login(T request);
}
