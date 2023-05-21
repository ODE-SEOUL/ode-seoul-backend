package kr.njw.odeseoul.recruit.application;

import kr.njw.odeseoul.recruit.application.dto.CreateRecruitRequest;
import kr.njw.odeseoul.recruit.application.dto.CreateRecruitResponse;

public interface RecruitService {
    CreateRecruitResponse createRecruit(CreateRecruitRequest request);
}
