package kr.njw.odeseoul.auth.application;

import kr.njw.odeseoul.auth.application.dto.SignupRequest;
import kr.njw.odeseoul.auth.application.dto.SignupResponse;

public interface SignupService {
    SignupResponse signup(SignupRequest request);
}
