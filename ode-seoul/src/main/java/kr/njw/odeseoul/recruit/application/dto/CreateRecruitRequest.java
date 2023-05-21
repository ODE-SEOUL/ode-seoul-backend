package kr.njw.odeseoul.recruit.application.dto;

import kr.njw.odeseoul.recruit.entity.Recruit;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateRecruitRequest {
    private Long hostUserId;
    private Long courseId;
    private Recruit.RecruitCategory category;
    private String title;
    private String content;
    private String image;
    private int maxPeople;
    private LocalDateTime scheduledAt;
}
