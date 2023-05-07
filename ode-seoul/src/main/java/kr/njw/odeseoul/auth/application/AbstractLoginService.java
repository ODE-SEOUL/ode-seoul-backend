package kr.njw.odeseoul.auth.application;

import jakarta.transaction.Transactional;
import kr.njw.odeseoul.auth.application.dto.AbstractLoginRequest;
import kr.njw.odeseoul.auth.application.dto.LoginResponse;
import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.common.security.JwtAuthenticationProvider;
import kr.njw.odeseoul.common.security.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
public abstract class AbstractLoginService<T extends AbstractLoginRequest> implements LoginService<T> {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    protected abstract AuthenticationResult authenticate(T request);

    public LoginResponse login(T request) {
        AuthenticationResult authenticationResult;

        try {
            authenticationResult = this.authenticate(request);
        } catch (Exception e) {
            log.error("[Login Error]", e);
            throw new BaseException(BaseResponseStatus.UNAUTHORIZED);
        }

        LoginResponse response = new LoginResponse();
        response.setId(authenticationResult.getId()); // TODO: 서비스 자체 ID로 변경
        response.getSocialProfile().setNickname(authenticationResult.getSocialProfile().getNickname());
        response.setAccessToken(this.jwtAuthenticationProvider.createToken(response.getId(), List.of(Role.USER)));
        response.setRefreshToken(RandomStringUtils.randomAlphanumeric(32)); // TODO: 리프레시 토큰 발급 구현
        response.setStatus(LoginResponse.LoginStatus.BEFORE_REGISTERED); // TODO: 가입 로직 구현
        return response;
    }

    @Data
    public static class AuthenticationResult {
        private String id;
        private SocialProfile socialProfile = new SocialProfile();

        @Data
        public static class SocialProfile {
            private String nickname;
        }
    }
}
