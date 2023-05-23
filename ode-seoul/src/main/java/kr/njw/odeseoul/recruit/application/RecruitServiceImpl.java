package kr.njw.odeseoul.recruit.application;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.course.entity.Course;
import kr.njw.odeseoul.course.repository.CourseRepository;
import kr.njw.odeseoul.recruit.application.dto.*;
import kr.njw.odeseoul.recruit.entity.Recruit;
import kr.njw.odeseoul.recruit.entity.RecruitApplication;
import kr.njw.odeseoul.recruit.entity.RecruitComment;
import kr.njw.odeseoul.recruit.repository.RecruitApplicationRepository;
import kr.njw.odeseoul.recruit.repository.RecruitCommentRepository;
import kr.njw.odeseoul.recruit.repository.RecruitRepository;
import kr.njw.odeseoul.user.entity.User;
import kr.njw.odeseoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class RecruitServiceImpl implements RecruitService {
    private final RecruitRepository recruitRepository;
    private final RecruitApplicationRepository recruitApplicationRepository;
    private final RecruitCommentRepository recruitCommentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public CreateRecruitResponse createRecruit(CreateRecruitRequest request) {
        User host = this.userRepository.findByIdAndDeletedAtIsNull(request.getHostUserId()).orElse(null);
        Course course = this.courseRepository.findByIdAndDeletedAtIsNull(request.getCourseId()).orElse(null);

        if (host == null) {
            throw new BaseException(BaseResponseStatus.CREATE_RECRUIT_ERROR_NOT_FOUND_HOST);
        }

        if (course == null) {
            throw new BaseException(BaseResponseStatus.CREATE_RECRUIT_ERROR_NOT_FOUND_COURSE);
        }

        Recruit recruit = Recruit.builder()
                .host(host)
                .course(course)
                .category(request.getCategory())
                .title(StringUtils.trimToEmpty(request.getTitle()))
                .content(StringUtils.trimToEmpty(request.getContent()))
                .image(StringUtils.trimToEmpty(request.getImage()))
                .currentPeople(1)
                .maxPeople(request.getMaxPeople())
                .scheduledAt(request.getScheduledAt())
                .progressStatus(Recruit.RecruitProgressStatus.OPEN)
                .build();

        this.recruitRepository.saveAndFlush(recruit);

        CreateRecruitResponse response = new CreateRecruitResponse();
        response.setId(recruit.getId());
        return response;
    }

    public ApplyRecruitResponse applyRecruit(ApplyRecruitRequest request) {
        Recruit recruit = this.recruitRepository.findForUpdateByIdAndDeletedAtIsNull(request.getRecruitId()).orElse(null);
        User member = this.userRepository.findByIdAndDeletedAtIsNull(request.getMemberUserId()).orElse(null);

        if (recruit == null) {
            throw new BaseException(BaseResponseStatus.APPLY_RECRUIT_ERROR_NOT_FOUND_RECRUIT);
        }

        if (member == null) {
            throw new BaseException(BaseResponseStatus.APPLY_RECRUIT_ERROR_NOT_FOUND_MEMBER);
        }

        if (recruit.getHost() == member) {
            throw new BaseException(BaseResponseStatus.APPLY_RECRUIT_ERROR_I_AM_HOST);
        }

        if (this.recruitApplicationRepository.findByRecruitIdAndMemberIdAndDeletedAtIsNull(recruit.getId(), member.getId()).isPresent()) {
            throw new BaseException(BaseResponseStatus.APPLY_RECRUIT_ERROR_ALREADY_APPLIED);
        }

        if (recruit.getProgressStatus() != Recruit.RecruitProgressStatus.OPEN) {
            throw new BaseException(BaseResponseStatus.APPLY_RECRUIT_ERROR_NOT_OPENED_RECRUIT);
        }

        if (recruit.isFull()) {
            throw new BaseException(BaseResponseStatus.APPLY_RECRUIT_ERROR_RECRUIT_FULL);
        }

        RecruitApplication recruitApplication = RecruitApplication.builder()
                .recruit(recruit)
                .member(member)
                .build();

        this.recruitApplicationRepository.saveAndFlush(recruitApplication);
        recruit.increaseMember();

        ApplyRecruitResponse response = new ApplyRecruitResponse();
        response.setId(recruitApplication.getId());
        response.setCreatedAt(recruitApplication.getCreatedAt());
        return response;
    }

    public void cancelRecruitApplication(CancelRecruitApplicationRequest request) {
        Recruit recruit = this.recruitRepository.findForUpdateByIdAndDeletedAtIsNull(request.getRecruitId()).orElse(null);
        RecruitApplication recruitApplication = this.recruitApplicationRepository.findByRecruitIdAndMemberIdAndDeletedAtIsNull(
                request.getRecruitId(), request.getMemberUserId()).orElse(null);

        if (recruitApplication == null) {
            return;
        }

        recruitApplication.delete();

        if (recruit == null) {
            return;
        }

        recruit.decreaseMember();
    }

    public WriteCommentResponse writeComment(WriteCommentRequest request) {
        Recruit recruit = this.recruitRepository.findByIdAndDeletedAtIsNull(request.getRecruitId()).orElse(null);
        User user = this.userRepository.findByIdAndDeletedAtIsNull(request.getUserId()).orElse(null);

        if (recruit == null) {
            throw new BaseException(BaseResponseStatus.WRITE_COMMENT_ERROR_NOT_FOUND_RECRUIT);
        }

        if (user == null) {
            throw new BaseException(BaseResponseStatus.WRITE_COMMENT_ERROR_NOT_FOUND_USER);
        }

        RecruitComment recruitComment = RecruitComment.builder()
                .recruit(recruit)
                .user(user)
                .content(StringUtils.trimToEmpty(request.getContent()))
                .build();

        this.recruitCommentRepository.saveAndFlush(recruitComment);

        WriteCommentResponse response = new WriteCommentResponse();
        response.setId(recruitComment.getId());
        response.setContent(recruitComment.getContent());
        response.setCreatedAt(recruitComment.getCreatedAt());
        return response;
    }

    public void deleteComment(DeleteCommentRequest request) {
        RecruitComment recruitComment = this.recruitCommentRepository.findByIdAndDeletedAtIsNull(request.getRecruitCommentId()).orElse(null);

        if (recruitComment == null) {
            return;
        }

        if (!Objects.equals(recruitComment.getUser().getId(), request.getUserId())) {
            throw new BaseException(BaseResponseStatus.DELETE_COMMENT_ERROR_NOT_MY_COMMENT);
        }

        recruitComment.delete();
    }
}
