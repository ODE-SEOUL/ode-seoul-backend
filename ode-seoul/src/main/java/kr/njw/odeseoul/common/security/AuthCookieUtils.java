package kr.njw.odeseoul.common.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.njw.odeseoul.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;

public class AuthCookieUtils {
    private static final String ACCESS_TOKEN_KEY = "ode_seoul_access_token";
    private static final String REFRESH_TOKEN_KEY = "ode_seoul_refresh_token";

    public static void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
        CookieUtils.addCookie(response, ACCESS_TOKEN_KEY, accessToken);
    }

    public static void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        CookieUtils.addCookie(response, REFRESH_TOKEN_KEY, refreshToken);
    }

    public static String getAccessTokenCookie(HttpServletRequest request) {
        Cookie cookie = CookieUtils.getCookie(request, ACCESS_TOKEN_KEY).orElse(null);

        if (cookie != null) {
            return StringUtils.trimToEmpty(cookie.getValue());
        }

        return "";
    }

    public static String getRefreshTokenCookie(HttpServletRequest request) {
        Cookie cookie = CookieUtils.getCookie(request, REFRESH_TOKEN_KEY).orElse(null);

        if (cookie != null) {
            return StringUtils.trimToEmpty(cookie.getValue());
        }

        return "";
    }
}
