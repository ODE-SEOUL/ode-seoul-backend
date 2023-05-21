package kr.njw.odeseoul.recruit.application;

import kr.njw.odeseoul.recruit.application.dto.SearchRecruitsRequest;
import kr.njw.odeseoul.recruit.application.dto.SearchRecruitsResponse;

public interface RecruitProvider {
    SearchRecruitsResponse searchRecruits(SearchRecruitsRequest request);
}
