package kr.njw.odeseoul.recruit.application.dto;

import kr.njw.odeseoul.recruit.entity.Recruit;
import lombok.Data;

@Data
public class ChangeRecruitProgressRequest {
    private Long recruitId;
    private Long hostUserId;
    private Recruit.RecruitProgressStatus progressStatus;
}
