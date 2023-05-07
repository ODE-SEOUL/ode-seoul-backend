package kr.njw.odeseoul.auth.application.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KakaoLoginRequest extends AbstractLoginRequest {
    private String authorizationCode;
    private String redirectedUri;
}
