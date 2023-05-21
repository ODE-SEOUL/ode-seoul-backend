package kr.njw.odeseoul.recruit.repository.dto;

import kr.njw.odeseoul.recruit.entity.Recruit;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class SearchRecruitsRepoRequest {
    private Recruit.RecruitCategory category;
    private Long courseId;
    private Long hostUserId;
    private Long memberUserId;
    private List<Recruit.RecruitProgressStatus> progressStatusOneOf;
    private String titleContains;
    private Pageable pageable;
}
