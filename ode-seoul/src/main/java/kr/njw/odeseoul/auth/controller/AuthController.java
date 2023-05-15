package kr.njw.odeseoul.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.njw.odeseoul.auth.application.AuthService;
import kr.njw.odeseoul.auth.application.LoginService;
import kr.njw.odeseoul.auth.application.SignupService;
import kr.njw.odeseoul.auth.application.dto.*;
import kr.njw.odeseoul.auth.controller.dto.KakaoLoginRestRequest;
import kr.njw.odeseoul.auth.controller.dto.SignupRestRequest;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.common.security.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final LoginService<KakaoLoginRequest> kakaoLoginService;
    private final SignupService signupService;

    @Operation(
            summary = "카카오 로그인",
            description = """
                    프론트에서 카카오 인증 요청 후 리다이렉션으로 받은 '인가 코드'를 통해 카카오 로그인을 완료하는 API

                    로그인 후 엑세스 토큰과 리프레시 토큰이 새로 발급됨. 기존 엑세스 토큰과 리프레시 토큰은 더이상 사용할 수 없음"""
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    headers = {
                            @Header(name = "Set-Cookie", description = "리프레시 토큰 (HttpOnly 세션쿠키)",
                                    schema = @Schema(example = "ode_seoul_refresh_token=Lwj8TE5Lr97EnRRQYbyP1Zu4h4tBht4i; Path=/; HttpOnly")
                            )
                    }),
            @ApiResponse(responseCode = "401", description = "로그인에 실패했습니다. (code: 10000)", content = @Content())
    })
    @PostMapping("/accounts/login/kakao")
    public ResponseEntity<BaseResponse<LoginResponse>> kakaoLogin(HttpServletResponse httpServletResponse,
                                                                  @Valid @RequestBody KakaoLoginRestRequest restRequest) {
        KakaoLoginRequest request = new KakaoLoginRequest();
        request.setAuthorizationCode(restRequest.getAuthorizationCode());
        request.setRedirectedUri(restRequest.getRedirectedUri());

        LoginResponse response = this.kakaoLoginService.login(request);

        AuthUtils.setRefreshTokenCookie(httpServletResponse, response.getRefreshToken());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @SecurityRequirement(name = "accessToken")
    @Operation(
            summary = "회원가입",
            description = """
                    소셜 로그인 후 최초 1회 회원가입을 하는 API (소셜 로그인 -> 회원가입 플로우)

                    반드시 소셜 로그인 API를 먼저 호출하여 엑세스 토큰을 받은 상태에서, 회원가입 API 호출

                    회원가입을 완료해야 회원가입 API 외 엑세스 토큰을 요구하는 API를 호출할 수 있음 (회원가입 미완료 상태로 호출시 code 403 발생)

                    또한 회원가입 미완료 상태일 때만 회원가입 API 호출 요망 (이미 회원가입이 완료된 엑세스 토큰으로 호출시 code 403 발생)

                    회원가입 완료 후 엑세스 토큰과 리프레시 토큰이 새로 발급됨. 기존 엑세스 토큰과 리프레시 토큰은 더이상 사용할 수 없음"""
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    headers = {
                            @Header(name = "Set-Cookie", description = "리프레시 토큰 (HttpOnly 세션쿠키)",
                                    schema = @Schema(example = "ode_seoul_refresh_token=Lwj8TE5Lr97EnRRQYbyP1Zu4h4tBht4i; Path=/; HttpOnly")
                            )
                    }),
            @ApiResponse(responseCode = "400", description = "지역 정보가 올바르지 않습니다. (code: 10101)", content = @Content()),
            @ApiResponse(responseCode = "401", description = "계정 상태가 올바르지 않습니다. (code: 10100)", content = @Content())
    })
    @PostMapping("/accounts/signup")
    public ResponseEntity<BaseResponse<SignupResponse>> signup(HttpServletResponse httpServletResponse,
                                                               Principal principal,
                                                               @Valid @RequestBody SignupRestRequest restRequest) {
        SignupRequest request = new SignupRequest();
        request.setId(Long.valueOf(principal.getName()));
        request.setNickname(restRequest.getNickname());
        request.setLocationCode(restRequest.getLocationCode());

        SignupResponse response = this.signupService.signup(request);

        AuthUtils.setRefreshTokenCookie(httpServletResponse, response.getRefreshToken());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @SecurityRequirement(name = "refreshToken")
    @Operation(
            summary = "토큰 갱신",
            description = """
                    리프레시 토큰을 통해, 엑세스 토큰과 리프레시 토큰을 재발급받는 API

                    현재 보유 중인 엑세스 토큰이 만료된 경우에 호출 (엑세스 토큰을 요구하는 API에서 code 401이 발생하면 만료된 것)

                    갱신 후 기존 엑세스 토큰과 리프레시 토큰은 더이상 사용할 수 없음"""
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    headers = {
                            @Header(name = "Set-Cookie", description = "리프레시 토큰 (HttpOnly 세션쿠키)",
                                    schema = @Schema(example = "ode_seoul_refresh_token=Lwj8TE5Lr97EnRRQYbyP1Zu4h4tBht4i; Path=/; HttpOnly")
                            )
                    }),
            @ApiResponse(responseCode = "401", description = "리프레시 토큰이 올바르지 않습니다. (code: 10200)", content = @Content())
    })
    @PatchMapping("/accounts/token")
    public ResponseEntity<BaseResponse<RenewTokenResponse>> renewAccessToken(HttpServletRequest httpServletRequest,
                                                                             HttpServletResponse httpServletResponse) {
        RenewTokenRequest request = new RenewTokenRequest();
        request.setRefreshToken(AuthUtils.getRefreshToken(httpServletRequest));

        RenewTokenResponse response = this.authService.renewToken(request);

        AuthUtils.setRefreshTokenCookie(httpServletResponse, response.getRefreshToken());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}
