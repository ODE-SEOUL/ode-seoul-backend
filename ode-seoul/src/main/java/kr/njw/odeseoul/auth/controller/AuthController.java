package kr.njw.odeseoul.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.njw.odeseoul.auth.application.LoginService;
import kr.njw.odeseoul.auth.application.dto.KakaoLoginRequest;
import kr.njw.odeseoul.auth.application.dto.LoginResponse;
import kr.njw.odeseoul.auth.controller.dto.KakaoLoginRestRequest;
import kr.njw.odeseoul.auth.controller.dto.LoginRestResponse;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.common.security.AuthCookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final LoginService<KakaoLoginRequest> kakaoLoginService;

    @PostMapping("/accounts/kakao/tokens")
    public ResponseEntity<BaseResponse<LoginRestResponse>> kakaoLogin(HttpServletResponse httpServletResponse,
                                                                      @Valid @RequestBody KakaoLoginRestRequest restRequest) {
        KakaoLoginRequest request = new KakaoLoginRequest();
        request.setAuthorizationCode(restRequest.getAuthorizationCode());
        request.setRedirectedUri(restRequest.getRedirectedUri());

        LoginResponse response = this.kakaoLoginService.login(request);
        LoginRestResponse restResponse = this.createLoginRestResponse(response);

        AuthCookieUtils.addAccessTokenCookie(httpServletResponse, restResponse.getAccessToken());
        AuthCookieUtils.addRefreshTokenCookie(httpServletResponse, restResponse.getRefreshToken());
        return ResponseEntity.ok(new BaseResponse<>(restResponse));
    }

    private LoginRestResponse createLoginRestResponse(LoginResponse response) {
        LoginRestResponse restResponse = new LoginRestResponse();
        restResponse.setId(response.getId());
        restResponse.getSocialProfile().setNickname(response.getSocialProfile().getNickname());
        restResponse.setAccessToken(response.getAccessToken());
        restResponse.setRefreshToken(response.getRefreshToken());

        switch (response.getStatus()) {
            case BEFORE_REGISTERED -> restResponse.setStatus(LoginRestResponse.LoginStatus.BEFORE_REGISTERED);
            case REGISTERED -> restResponse.setStatus(LoginRestResponse.LoginStatus.REGISTERED);
        }

        return restResponse;
    }
}
