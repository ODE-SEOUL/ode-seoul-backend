package kr.njw.odeseoul.recruit.application;

import kr.njw.odeseoul.recruit.application.dto.*;

public interface RecruitService {
    CreateRecruitResponse createRecruit(CreateRecruitRequest request);

    void changeRecruitProgress(ChangeRecruitProgressRequest request);

    ApplyRecruitResponse applyRecruit(ApplyRecruitRequest request);

    void cancelRecruitApplication(CancelRecruitApplicationRequest request);

    WriteCommentResponse writeComment(WriteCommentRequest request);

    void deleteComment(DeleteCommentRequest request);
}
