package kr.njw.odeseoul.recruit.application.dto;

import lombok.Data;

@Data
public class ApplyRecruitRequest {
    private Long recruitId;
    private Long memberUserId;
}
