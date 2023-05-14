package kr.njw.odeseoul.auth.application.dto;

import lombok.Data;

@Data
public class RenewTokenRequest {
    private String refreshToken;
}
