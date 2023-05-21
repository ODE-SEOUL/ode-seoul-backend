package kr.njw.odeseoul.recruit.application;

import kr.njw.odeseoul.recruit.application.dto.FindRecruitRequest;
import kr.njw.odeseoul.recruit.application.dto.FindRecruitResponse;
import kr.njw.odeseoul.recruit.application.dto.SearchRecruitsRequest;
import kr.njw.odeseoul.recruit.application.dto.SearchRecruitsResponse;

public interface RecruitProvider {
    FindRecruitResponse findRecruit(FindRecruitRequest request);

    SearchRecruitsResponse searchRecruits(SearchRecruitsRequest request);
}
