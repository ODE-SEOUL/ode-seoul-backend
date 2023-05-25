package kr.njw.odeseoul.auth.application;

import kr.njw.odeseoul.auth.application.dto.AbstractLoginRequest;
import kr.njw.odeseoul.auth.application.dto.LoginResponse;
import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.common.security.JwtAuthenticationProvider;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
public abstract class AbstractLoginService<T extends AbstractLoginRequest> implements LoginService<T> {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserRepository userRepository;

    protected abstract AuthenticationResult authenticate(T request);

    public LoginResponse login(T request) {
        AuthenticationResult authenticationResult;

        try {
            authenticationResult = this.authenticate(request);
        } catch (Exception e) {
            log.error("[{}] message: {}", e.getClass().getName(), e.getMessage());
            throw new BaseException(BaseResponseStatus.LOGIN_ERROR);
        }

        User user = this.userRepository.findByLoginIdAndDeletedAtIsNull(authenticationResult.getId()).orElse(null);

        if (user == null) {
            user = User.builder()
                    .nickname(authenticationResult.getSocialProfile().getNickname())
                    .profileImage(authenticationResult.getSocialProfile().getProfileImage())
                    .loginId(authenticationResult.getId())
                    .loginPw("")
                    .refreshToken("")
                    .signupStatus(User.UserSignupStatus.BEFORE_REGISTERED)
                    .build();
        }

        user.renewRefreshToken();
        this.userRepository.saveAndFlush(user);

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.getSocialProfile().setNickname(authenticationResult.getSocialProfile().getNickname());
        response.getSocialProfile().setProfileImage(authenticationResult.getSocialProfile().getProfileImage());
        response.setAccessToken(this.jwtAuthenticationProvider.createToken(String.valueOf(response.getId()), List.of(user.getRole())));
        response.setRefreshToken(user.getRefreshToken());
        response.setSignupStatus(user.getSignupStatus());
        return response;
    }

    @Data
    public static class AuthenticationResult {
        private String id;
        private SocialProfile socialProfile = new SocialProfile();

        @Data
        public static class SocialProfile {
            private String nickname;
            private String profileImage;
        }
    }
}
