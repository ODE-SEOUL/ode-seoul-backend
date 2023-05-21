package kr.njw.odeseoul.recruit.repository;

import kr.njw.odeseoul.recruit.entity.Recruit;
import kr.njw.odeseoul.recruit.repository.dto.SearchRecruitsRepoRequest;
import org.springframework.data.domain.Page;

public interface RecruitRepositoryCustom {
    Page<Recruit> search(SearchRecruitsRepoRequest request);
}
