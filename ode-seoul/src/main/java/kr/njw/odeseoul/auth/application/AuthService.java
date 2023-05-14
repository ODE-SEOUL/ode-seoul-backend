package kr.njw.odeseoul.auth.application;

import kr.njw.odeseoul.auth.application.dto.RenewTokenRequest;
import kr.njw.odeseoul.auth.application.dto.RenewTokenResponse;

public interface AuthService {
    RenewTokenResponse renewToken(RenewTokenRequest refreshToken);
}
