package kr.njw.odeseoul.common.config;

import kr.njw.odeseoul.common.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SpringSecurityConfig {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/v3/api-docs*/**", "/swagger-ui*/**", "/actuator/**");

        httpSecurity
                .authorizeHttpRequests()
                .anyRequest().hasRole(Role.ADMIN.getValue());

        httpSecurity
                .httpBasic().and()
                .formLogin().disable()
                .rememberMe().disable()
                .logout().disable()
                .csrf().disable()
                .cors().and()
                .headers().defaultsDisabled().cacheControl().and().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers("/auth/accounts/signup/**").hasRole(Role.GUEST.getValue())
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/courses/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/locations/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/notices/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/recruits").permitAll()
                .requestMatchers(HttpMethod.GET, "/recruits/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/{id}").permitAll()
                .anyRequest().hasRole(Role.USER.getValue());

        httpSecurity
                .httpBasic().disable()
                .formLogin().disable()
                .rememberMe().disable()
                .logout().disable()
                .csrf().disable()
                .cors().and()
                .headers().defaultsDisabled().cacheControl().and().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling()
                .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
                .accessDeniedHandler(this.jwtAccessDeniedHandler).and()
                .addFilterBefore(new JwtAuthenticationFilter(this.jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**", "/actuator/health/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(List.of("*://localhost:[*]", "*://127.0.0.1:[*]"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
