package kr.njw.odeseoul.auth.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class LoginRestResponse {
    private String id;
    private SocialProfile socialProfile = new SocialProfile();
    private String accessToken;
    private String refreshToken;
    private LoginStatus status;

    @Data
    public static class SocialProfile {
        private String nickname;
    }

    @AllArgsConstructor
    @Getter
    public enum LoginStatus {
        BEFORE_REGISTERED(1),
        REGISTERED(2);

        private final int value;
    }
}
