package kr.njw.odeseoul.recruit.application.dto;

import lombok.Data;

@Data
public class CancelRecruitApplicationRequest {
    private Long recruitId;
    private Long memberUserId;
}
