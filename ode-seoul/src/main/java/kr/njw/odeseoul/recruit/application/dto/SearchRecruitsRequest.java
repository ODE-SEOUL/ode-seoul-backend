package kr.njw.odeseoul.recruit.application.dto;

import kr.njw.odeseoul.recruit.entity.Recruit;
import lombok.Data;

@Data
public class SearchRecruitsRequest {
    private Recruit.RecruitCategory category;
    private Long courseId;
    private Long hostUserId;
    private Long memberUserId;
    private String titleContains;
    private Boolean open;
    private int pageNumber;
    private int pagingSize;
}
