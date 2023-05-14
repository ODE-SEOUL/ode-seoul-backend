package kr.njw.odeseoul.auth.application;

import kr.njw.odeseoul.auth.application.dto.RenewTokenRequest;
import kr.njw.odeseoul.auth.application.dto.RenewTokenResponse;
import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.common.security.JwtAuthenticationProvider;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthServiceImpl implements AuthService {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserRepository userRepository;

    public RenewTokenResponse renewToken(RenewTokenRequest request) {
        User user = this.userRepository.findByRefreshTokenAndDeletedAtIsNull(request.getRefreshToken()).orElse(null);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.RENEW_TOKEN_ERROR_BAD_USER);
        }

        user.renewRefreshToken();

        RenewTokenResponse response = new RenewTokenResponse();
        response.setAccessToken(this.jwtAuthenticationProvider.createToken(String.valueOf(user.getId()), List.of(user.getRole())));
        response.setRefreshToken(user.getRefreshToken());
        return response;
    }
}
