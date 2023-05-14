package kr.njw.odeseoul.auth.application;

import kr.njw.odeseoul.auth.application.dto.SignupRequest;
import kr.njw.odeseoul.auth.application.dto.SignupResponse;
import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.common.security.JwtAuthenticationProvider;
import kr.njw.odeseoul.location.entity.Location;
import kr.njw.odeseoul.location.repository.LocationRepository;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class SignupServiceImpl implements SignupService {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public SignupResponse signup(SignupRequest request) {
        User user = this.userRepository.findByIdAndDeletedAtIsNull(request.getId()).orElse(null);
        Location location = this.locationRepository.findByCodeAndDeletedAtIsNull(request.getLocationCode()).orElse(null);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.SIGNUP_ERROR_BAD_USER);
        }

        if (user.getSignupStatus() != User.UserSignupStatus.BEFORE_REGISTERED) {
            throw new BaseException(BaseResponseStatus.SIGNUP_ERROR_BAD_USER);
        }

        if (StringUtils.isNotBlank(request.getLocationCode()) && location == null) {
            throw new BaseException(BaseResponseStatus.SIGNUP_ERROR_BAD_LOCATION);
        }

        if (location != null && !location.isSeoulGugun()) {
            throw new BaseException(BaseResponseStatus.SIGNUP_ERROR_BAD_LOCATION);
        }

        user.signup(request.getNickname(), location);
        user.renewRefreshToken();

        SignupResponse response = new SignupResponse();
        response.setId(request.getId());
        response.setAccessToken(this.jwtAuthenticationProvider.createToken(String.valueOf(response.getId()), List.of(user.getRole())));
        response.setRefreshToken(user.getRefreshToken());
        return response;
    }
}
