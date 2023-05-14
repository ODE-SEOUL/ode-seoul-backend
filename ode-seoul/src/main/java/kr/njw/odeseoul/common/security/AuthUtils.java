package kr.njw.odeseoul.common.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.njw.odeseoul.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthUtils {
    public static final String REFRESH_TOKEN_KEY = "ode_seoul_refresh_token";
    private static final String BEARER_PREFIX = "Bearer ";

    public static void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        CookieUtils.addCookie(response, REFRESH_TOKEN_KEY, refreshToken);
    }

    public static String getAccessToken(HttpServletRequest request) {
        return AuthUtils.getBearerToken(request);
    }

    public static String getRefreshToken(HttpServletRequest request) {
        String bearerToken = AuthUtils.getBearerToken(request);

        if (!bearerToken.equals("")) {
            return bearerToken;
        }

        return AuthUtils.getRefreshTokenCookie(request);
    }

    private static String getBearerToken(HttpServletRequest request) {
        String authHeader = StringUtils.trimToEmpty(request.getHeader(AUTHORIZATION));
        return StringUtils.trimToEmpty(StringUtils.substringAfter(authHeader, BEARER_PREFIX));
    }

    private static String getRefreshTokenCookie(HttpServletRequest request) {
        Cookie cookie = CookieUtils.getCookie(request, REFRESH_TOKEN_KEY).orElse(null);

        if (cookie != null) {
            return StringUtils.trimToEmpty(cookie.getValue());
        }

        return "";
    }
}
