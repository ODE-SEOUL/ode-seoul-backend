package kr.njw.odeseoul.auth.application.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private Long id;
    private String nickname;
    private String locationCode;
}
