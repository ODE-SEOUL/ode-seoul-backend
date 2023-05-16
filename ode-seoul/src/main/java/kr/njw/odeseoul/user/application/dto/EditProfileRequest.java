package kr.njw.odeseoul.user.application.dto;

import lombok.Data;

@Data
public class EditProfileRequest {
    private Long id;
    private String nickname;
    private String profileImage;
    private String locationCode;
}
