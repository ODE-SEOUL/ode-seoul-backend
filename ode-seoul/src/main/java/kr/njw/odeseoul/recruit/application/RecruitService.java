package kr.njw.odeseoul.recruit.application;

import kr.njw.odeseoul.recruit.application.dto.*;

public interface RecruitService {
    CreateRecruitResponse createRecruit(CreateRecruitRequest request);

    ApplyRecruitResponse applyRecruit(ApplyRecruitRequest request);

    void cancelRecruitApplication(CancelRecruitApplicationRequest request);
}
