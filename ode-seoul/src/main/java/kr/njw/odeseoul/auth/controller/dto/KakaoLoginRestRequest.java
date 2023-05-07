package kr.njw.odeseoul.auth.controller.dto;

import lombok.Data;

@Data
public class KakaoLoginRestRequest {
    private String authorizationCode;
    private String redirectedUri;
}
