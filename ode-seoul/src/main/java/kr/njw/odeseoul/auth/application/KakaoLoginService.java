package kr.njw.odeseoul.auth.application;

import kr.njw.odeseoul.auth.application.dto.KakaoLoginRequest;
import kr.njw.odeseoul.common.security.JwtAuthenticationProvider;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Service
public class KakaoLoginService extends AbstractLoginService<KakaoLoginRequest> {
    private static final String KAKAO_AUTH_API_HOST = "https://kauth.kakao.com";
    private static final String KAKAO_MAIN_API_HOST = "https://kapi.kakao.com";
    private static final String KAKAO_AUTH_API_REQUEST_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String KAKAO_MAIN_API_REQUEST_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private static final URI KAKAO_AUTH_API_CREATE_TOKEN_URI = UriComponentsBuilder.fromHttpUrl(KAKAO_AUTH_API_HOST).path("/oauth/token").build().toUri();
    private static final URI KAKAO_MAIN_API_GET_PROFILE_URI = UriComponentsBuilder.fromHttpUrl(KAKAO_MAIN_API_HOST).path("/v2/user/me").build().toUri();
    private static final String KAKAO_ID_PREFIX = "kakao_";

    private final RestTemplate restTemplate;
    private final String clientId;

    public KakaoLoginService(JwtAuthenticationProvider jwtAuthenticationProvider,
                             UserRepository userRepository,
                             RestTemplate restTemplate,
                             @Value("${ode-seoul.kakao.client-id}") String clientId) {
        super(jwtAuthenticationProvider, userRepository);
        this.restTemplate = restTemplate;
        this.clientId = clientId;
    }

    protected AuthenticationResult authenticate(KakaoLoginRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, KAKAO_AUTH_API_REQUEST_CONTENT_TYPE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", this.clientId);
        body.add("redirect_uri", request.getRedirectedUri());
        body.add("code", request.getAuthorizationCode());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<CreateTokenApiResponse> responseEntity = this.restTemplate.exchange(
                KAKAO_AUTH_API_CREATE_TOKEN_URI,
                HttpMethod.POST,
                httpEntity,
                CreateTokenApiResponse.class
        );

        GetProfileApiResponse profile = this.getProfile(Objects.requireNonNull(responseEntity.getBody()).getAccess_token());

        long oauthId = Objects.requireNonNull(profile.getId());
        String nickname = profile.getKakao_account().getProfile().getNickname();
        String profileImageUrl = profile.getKakao_account().getProfile().getProfile_image_url();

        AuthenticationResult authenticationResult = new AuthenticationResult();
        authenticationResult.setId(KAKAO_ID_PREFIX + oauthId);
        authenticationResult.getSocialProfile().setNickname(StringUtils.trimToEmpty(nickname));
        authenticationResult.getSocialProfile().setProfileImage(StringUtils.trimToEmpty(profileImageUrl));
        return authenticationResult;
    }

    private GetProfileApiResponse getProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + accessToken);
        headers.add(CONTENT_TYPE, KAKAO_MAIN_API_REQUEST_CONTENT_TYPE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<GetProfileApiResponse> responseEntity = this.restTemplate.exchange(
                KAKAO_MAIN_API_GET_PROFILE_URI,
                HttpMethod.GET,
                httpEntity,
                GetProfileApiResponse.class
        );

        return responseEntity.getBody();
    }

    @Data
    public static class CreateTokenApiResponse {
        private String access_token;
    }

    @Data
    public static class GetProfileApiResponse {
        private Long id;
        private KakaoAccount kakao_account;

        @Data
        public static class KakaoAccount {
            private Profile profile;

            @Data
            public static class Profile {
                private String nickname;
                private String profile_image_url;
            }
        }
    }
}
