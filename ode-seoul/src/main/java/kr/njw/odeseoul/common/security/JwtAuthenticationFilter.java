package kr.njw.odeseoul.common.security;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = StringUtils.trimToEmpty(request.getHeader(AUTHORIZATION));
        String token = null;

        // 1st. Authorization 헤더에서 토큰 추출
        // 2nd. 쿠키에서 토큰 추출
        if (StringUtils.startsWithIgnoreCase(authHeader, BEARER_PREFIX)) {
            token = StringUtils.substringAfter(authHeader, BEARER_PREFIX);
        } else {
            token = AuthCookieUtils.getAccessTokenCookie(request);
        }

        if (StringUtils.isNotBlank(token)) {
            Optional<Authentication> authentication = this.jwtAuthenticationProvider.getAuthentication(token);
            authentication.ifPresent(value -> SecurityContextHolder.getContext().setAuthentication(value));
        }

        filterChain.doFilter(request, response);
    }
}
